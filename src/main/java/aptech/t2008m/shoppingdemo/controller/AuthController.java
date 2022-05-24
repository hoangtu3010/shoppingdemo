package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.CurrentUserDetails;
import aptech.t2008m.shoppingdemo.entity.dto.CredentialDTO;
import aptech.t2008m.shoppingdemo.entity.enums.Roles;
import aptech.t2008m.shoppingdemo.service.AccountService;
import aptech.t2008m.shoppingdemo.until.CurrentUser;
import aptech.t2008m.shoppingdemo.until.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public ResponseEntity<?> register(@RequestBody Account account){
        if (accountService.existsAccount(account.getUserName())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        Account result = accountService.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user")
    public ResponseEntity<CurrentUserDetails> getCurrentUser(){
        Optional<Account> optionalAccount = accountService.findByUsername(CurrentUser.getCurrentUser().getName());

        if (!optionalAccount.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Account account = optionalAccount.get();

        CurrentUserDetails currentUserDetails = new CurrentUserDetails(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(currentUserDetails);
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
            if (account.getRoleId() == Roles.USER){
                authorities.add(new SimpleGrantedAuthority("user"));
            }
            else if (account.getRoleId() == Roles.ADMIN){
                authorities.add(new SimpleGrantedAuthority("admin"));
            }
            String accessToken = JwtUtil.generateToken(
                    account.getUserName(),
                    CurrentUser.getCurrentUser().getAuthorities().iterator().next().getAuthority(),
                    request.getRequestURL().toString(),
                    JwtUtil.ONE_DAY * 7);

            String refreshToken = JwtUtil.generateToken(
                    account.getUserName(),
                    CurrentUser.getCurrentUser().getAuthorities().iterator().next().getAuthority(),
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
