package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.ShoppingCart;
import aptech.t2008m.shoppingdemo.entity.dto.ShoppingCartDTO;
import aptech.t2008m.shoppingdemo.service.ShoppingCartService;
import aptech.t2008m.shoppingdemo.until.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get-all")
    public ResponseEntity<List<ShoppingCart>> findAll() {
        return ResponseEntity.ok(shoppingCartService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ShoppingCart> save(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingCartService.save(shoppingCartDTO));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<ShoppingCart> findById(@PathVariable String id) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartService.findById(id);

        if (!shoppingCartOptional.isPresent()) {
            ResponseEntity.notFound();
        }

        return ResponseEntity.ok(shoppingCartOptional.get());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ShoppingCart> findByUserName() {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartService.findByAccountUserName(CurrentUser.getCurrentUser().getName());

        if (!shoppingCartOptional.isPresent()) {
            ResponseEntity.notFound();
        }

        ShoppingCart existShoppingCart = shoppingCartOptional.get();

        existShoppingCart.setCartItems(existShoppingCart.getCartItems().stream().filter(c -> c.getStatus() == 1).collect(Collectors.toSet()));

        return ResponseEntity.ok(existShoppingCart);
    }
}
