package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.Order;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.dto.OrderDTO;
import aptech.t2008m.shoppingdemo.entity.enums.OrderStatus;
import aptech.t2008m.shoppingdemo.service.AccountService;
import aptech.t2008m.shoppingdemo.service.OrderService;
import aptech.t2008m.shoppingdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Order>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<Order> findById(@PathVariable String id){
        Optional<Order> optionalOrder = orderService.findById(id);

        if (!optionalOrder.isPresent()) {
            ResponseEntity.notFound();
        }

        return ResponseEntity.ok(optionalOrder.get());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Order> save(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(orderDTO));
    }

    @RequestMapping(method = RequestMethod.GET, path = "add-to-order")
    public ResponseEntity<?> addProductToOrder(@RequestParam String productId, @RequestParam String orderId, @RequestParam String accountId){
        Optional<Order> optionalOrder = orderService.findById(orderId);
        Optional<Product> optionalProduct = productService.findById(productId);
        Optional<Account> optionalAccount = accountService.findById(accountId);
        if(!optionalOrder.isPresent() || !optionalProduct.isPresent() || !optionalAccount.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found!");
        }

        boolean result = orderService.addProductToOrder(optionalProduct.get(), optionalOrder.get(), optionalAccount.get());
        if (!result){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Add order error!!!");
        }

        return ResponseEntity.ok("Add order success!!!");
    }
}
