package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.enums.Roles;
import aptech.t2008m.shoppingdemo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findAccountByUserName(username);
        if (!optionalAccount.isPresent()){
            throw new UsernameNotFoundException("Invalid information.");
        }
        Account existsAccount = optionalAccount.get();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (existsAccount.getRoleId() == Roles.USER){
            authorities.add(new SimpleGrantedAuthority("user"));
        }
        else if (existsAccount.getRoleId() == Roles.ADMIN){
            authorities.add(new SimpleGrantedAuthority("admin"));
        }

        return new User(
                existsAccount.getUserName(), existsAccount.getPasswordHash(), authorities);
    }
}
