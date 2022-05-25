package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.enums.AccountStatus;
import aptech.t2008m.shoppingdemo.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Account>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/fake-account")
    public ResponseEntity<?> fakeAccount() {
        Account account = new Account();
        account.setStatus(AccountStatus.ACTIVE);
        account.setUserName("admin");
        accountService.save(account);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
}
