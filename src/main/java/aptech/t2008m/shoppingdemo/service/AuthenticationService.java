package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.CurrentUserDetails;
import aptech.t2008m.shoppingdemo.entity.Roles;
import aptech.t2008m.shoppingdemo.entity.enums.AccountStatus;
import aptech.t2008m.shoppingdemo.repository.AccountRepository;
import aptech.t2008m.shoppingdemo.until.CurrentUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findAccountByUserName(username);
        if (!optionalAccount.isPresent()){
            throw new UsernameNotFoundException("Invalid information.");
        }
        Account existsAccount = optionalAccount.get();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles r:
                existsAccount.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
        }

        return new User(
                existsAccount.getUserName(), existsAccount.getPasswordHash(), authorities);
    }

    public Account register(Account account){
        account.setPasswordHash(passwordEncoder.encode(account.getPasswordHash()));
        account.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    public CurrentUserDetails getCurrentUser(){
        Optional<Account> optionalAccount = accountRepository.findAccountByUserName(CurrentUser.getCurrentUser().getName());

        if (!optionalAccount.isPresent()){
            return null;
        }

        Account account = optionalAccount.get();

        return new CurrentUserDetails(account);
    }
}
