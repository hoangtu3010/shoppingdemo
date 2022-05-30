package aptech.t2008m.shoppingdemo.config;

import aptech.t2008m.shoppingdemo.service.AuthenticationService;
import aptech.t2008m.shoppingdemo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
//      securedEnabled = true,
//      jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final PasswordEncoder passwordEncoder;

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
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/v1/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/admin").hasAnyAuthority("admin");
        http.authorizeRequests().antMatchers("/api/v1/shopping-cart/**").hasAnyAuthority("user", "admin");
        http.authorizeRequests().antMatchers("/api/v1/orders/**").hasAnyAuthority("user", "admin");
//        List<Permission> permissions = permissionService.findAll();
//        for (Permission permission :
//                permissions) {
//            List<Roles> roles = new ArrayList<>(permission.getRoles());
//            String[] roleStr = new String[roles.size()];
//
//            for (int i = 0; i < roles.size(); i++) {
//                roleStr[i] = roles.get(i).getName();
//            }
//
//            if (permission.getMethod().isEmpty()) {
//                http.authorizeRequests().antMatchers(permission.getUrl()).hasAnyAuthority(roleStr);
//            }else {
//                http.authorizeRequests().antMatchers(permission.getMethod(), permission.getUrl()).hasAnyAuthority(roleStr);
//            }
//        }
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
