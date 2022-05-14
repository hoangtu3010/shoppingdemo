package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.CartItem;
import aptech.t2008m.shoppingdemo.entity.CartItemId;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.ShoppingCart;
import aptech.t2008m.shoppingdemo.entity.dto.CartItemDTO;
import aptech.t2008m.shoppingdemo.entity.dto.ProductDTO;
import aptech.t2008m.shoppingdemo.entity.dto.ShoppingCartDTO;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import aptech.t2008m.shoppingdemo.service.ProductService;
import aptech.t2008m.shoppingdemo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/shopping-cart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ShoppingCart>> findAll() {
        return ResponseEntity.ok(shoppingCartService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ShoppingCart> save(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = shoppingCartDTO.generateCart();
        Set<CartItem> setCartItem = new HashSet<>();
        for (CartItemDTO cartItemDTO :
                shoppingCartDTO.getCartItemDTOSet()) {
            Optional<Product> optionalProduct = productService.findById(cartItemDTO.getProductId());
            if(!optionalProduct.isPresent()){
                break;
            }
            Product product = optionalProduct.get();
            CartItem cartItem = CartItem.builder()
                    .id(new CartItemId(shoppingCart.getId(), product.getId()))
                    .productName(product.getName())
                    .productThumbnail(product.getThumbnail())
                    .quantity(cartItemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .shoppingCart(shoppingCart)
                    .build();

            shoppingCart.addTotalPrice(cartItem); // add tổng giá bigdecimal
            setCartItem.add(cartItem);
        }
        shoppingCart.setCartItems(setCartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingCartService.save(shoppingCart));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<ShoppingCart> findById(@PathVariable String id) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartService.findById(id);

        if (!shoppingCartOptional.isPresent()) {
            ResponseEntity.notFound();
        }

        return ResponseEntity.ok(shoppingCartOptional.get());
    }
}
