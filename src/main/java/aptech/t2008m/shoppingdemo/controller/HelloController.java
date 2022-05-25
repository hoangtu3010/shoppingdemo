package aptech.t2008m.shoppingdemo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping
public class HelloController {
    @GetMapping(path = "/")
    public String hello(){
        return "hello";
    }
}
