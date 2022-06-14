package aptech.t2008m.shoppingdemo.config;

import aptech.t2008m.shoppingdemo.entity.Permission;
import aptech.t2008m.shoppingdemo.entity.Roles;
import aptech.t2008m.shoppingdemo.repository.PermissionRepository;
import aptech.t2008m.shoppingdemo.service.PermissionService;
import aptech.t2008m.shoppingdemo.service.RolesService;
import aptech.t2008m.shoppingdemo.until.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;

public class AuthorizationFilter extends OncePerRequestFilter {
    private static final String[] IGNORE_PATHS = {"/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/token/refresh"};

    private final RolesService rolesService;

    public AuthorizationFilter(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //let login and register pass through
        String requestPath = request.getServletPath();
        if (Arrays.asList(IGNORE_PATHS).contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //get token in header then decode jwt token to get username and roles -> then set roles for request
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);
            String username = decodedJWT.getSubject();

            String[] roles = decodedJWT.getClaim(JwtUtil.ROLE_CLAIM_KEY).asArray(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            List<Roles> rolesList = rolesService.findAllByNameIn(roles);

//            stream(roles).forEach(role -> {
//                authorities.add(new SimpleGrantedAuthority(role));
//            });

            for (Roles role :
                    rolesList) {
                for (Permission permission :
                        role.getPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            //show error
            System.err.println(ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("error", ex.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        }
    }
}
