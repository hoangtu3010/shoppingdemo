package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.CurrentUserDetails;
import aptech.t2008m.shoppingdemo.entity.Roles;
import aptech.t2008m.shoppingdemo.entity.dto.CredentialDTO;
import aptech.t2008m.shoppingdemo.entity.dto.CurrentUserDetailsDTO;
import aptech.t2008m.shoppingdemo.service.AccountService;
import aptech.t2008m.shoppingdemo.service.AuthenticationService;
import aptech.t2008m.shoppingdemo.until.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public ResponseEntity<Object> register(@RequestBody Account account) {
        if (accountService.existsAccount(account.getUserName())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is already taken!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(account));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user")
    public ResponseEntity<CurrentUserDetails> getCurrentUser() {
        if (authenticationService.getCurrentUser() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(authenticationService.getCurrentUser());
    }

    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("require token in header");
        }
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);
            String username = decodedJWT.getSubject();
            //load account in the token
            Optional<Account> optionalAccount = accountService.findByUsername(username);
            if (optionalAccount.isPresent()) {
                return ResponseEntity.badRequest().body("Wrong token: Username not exist");
            }
            Account account = optionalAccount.get();

            //now return new token
            //generate tokens
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            for (Roles role:
                    account.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            String accessToken = JwtUtil.generateToken(
                    account.getUserName(),
                    authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                    request.getRequestURL().toString(),
                    JwtUtil.ONE_DAY * 7);

            String refreshToken = JwtUtil.generateToken(
                    account.getUserName(),
                    authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                    request.getRequestURL().toString(),
                    JwtUtil.ONE_DAY * 14);
            CredentialDTO credential = new CredentialDTO(accessToken, refreshToken);
            return ResponseEntity.ok(credential);
        } catch (Exception ex) {
            //show error
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
