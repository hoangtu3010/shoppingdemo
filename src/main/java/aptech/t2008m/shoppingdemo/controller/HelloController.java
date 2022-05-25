package aptech.t2008m.shoppingdemo.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/hello")
public class HelloController {
    @RequestMapping(method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }
}
