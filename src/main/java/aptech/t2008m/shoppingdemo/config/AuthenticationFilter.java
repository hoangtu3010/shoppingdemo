package aptech.t2008m.shoppingdemo.config;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.dto.CredentialDTO;
import aptech.t2008m.shoppingdemo.until.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Nhận thông tin đăng nhập
            String jsonData = request.getReader().lines().collect(Collectors.joining());
            Gson gson = new Gson();
            Account account = gson.fromJson(jsonData, Account.class);
            // Tạo ra thông tin check đăng nhập.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account.getUserName(), account.getPasswordHash());
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //get user that successfully login
        User user = (User) authentication.getPrincipal();

        //generate tokens
        String accessToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 7);

        String refreshToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 14);

        CredentialDTO credential = new CredentialDTO(accessToken, refreshToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), credential);
    }
}
