package aptech.t2008m.shoppingdemo.config;

import aptech.t2008m.shoppingdemo.entity.Permission;
import aptech.t2008m.shoppingdemo.service.AuthenticationService;
import aptech.t2008m.shoppingdemo.service.PermissionService;
import aptech.t2008m.shoppingdemo.service.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final RolesService rolesService;
    @Autowired
    private final PermissionService permissionService;

    @Bean(name = "authenticationManagerBean")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        List<Permission> permissionList = permissionService.findAll();
//        http.authorizeRequests().antMatchers("/api/v1/auth/**", "/api/v1/products/**", "/api/v1/categories/**").permitAll();
        for (Permission permission :
                permissionList) {
            if (permission.getMethod() != null){
                switch (permission.getMethod()) {
                    case GET:
                        http.authorizeRequests().antMatchers(HttpMethod.GET, permission.getUrl()).hasAuthority(permission.getName());
                        break;
                    case POST:
                        http.authorizeRequests().antMatchers(HttpMethod.POST, permission.getUrl()).hasAuthority(permission.getName());
                        break;
                    case PUT:
                        http.authorizeRequests().antMatchers(HttpMethod.PUT, permission.getUrl()).hasAuthority(permission.getName());
                        break;
                    case DELETE:
                        http.authorizeRequests().antMatchers(HttpMethod.DELETE, permission.getUrl()).hasAuthority(permission.getName());
                        break;
                }
            }else {
                http.authorizeRequests().antMatchers(permission.getUrl()).hasAuthority(permission.getName());
            }
        }

        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new AuthorizationFilter(rolesService), UsernamePasswordAuthenticationFilter.class);
    }

}
