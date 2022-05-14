package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.IdentityCard;
import aptech.t2008m.shoppingdemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET, path = "/test01")
    public ResponseEntity<?> test01() {
        Account account = new Account();
        IdentityCard identityCard = new IdentityCard();
        identityCard.setNumber("001122334455");
        identityCard.setFullName("Hoang Anh Tu");
        identityCard.setBirthDay(new Timestamp(System.currentTimeMillis()));
        identityCard.setGender(1);
        identityCard.setAddress("HN");
        identityCard.setDescription("DES");
        identityCard.setStatus(1);
        account.setStatus(1);
        account.setIdentityCard(identityCard); // tao quan he
        account.setUserName("admin");
        accountService.save(account);
        System.out.println(accountService.findAll().get(0).getIdentityCard());
        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }
}
