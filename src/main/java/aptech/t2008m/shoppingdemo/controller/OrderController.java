package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Order;
import aptech.t2008m.shoppingdemo.entity.dto.OrderDTO;
import aptech.t2008m.shoppingdemo.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Order>> findAll(
            @RequestParam(defaultValue = "1") Integer pageIndex,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            @RequestParam(defaultValue = "0") Integer status,
            @RequestParam(defaultValue = "") String accountId,
            @RequestParam(defaultValue = "") String userName,
            @RequestParam(defaultValue = "") String productName){

        return ResponseEntity.ok(orderService.getPage(pageIndex, pageSize, startDate, endDate, status, accountId, userName, productName));
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
}
