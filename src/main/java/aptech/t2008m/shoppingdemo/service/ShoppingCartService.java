package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.CartItem;
import aptech.t2008m.shoppingdemo.entity.CartItemId;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.ShoppingCart;
import aptech.t2008m.shoppingdemo.entity.dto.CartItemDTO;
import aptech.t2008m.shoppingdemo.entity.dto.ShoppingCartDTO;
import aptech.t2008m.shoppingdemo.entity.enums.CartItemStatus;
import aptech.t2008m.shoppingdemo.repository.ProductRepository;
import aptech.t2008m.shoppingdemo.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<ShoppingCart> findAll() {
        return shoppingCartRepository.findAll();
    }

    public Optional<ShoppingCart> findById(String id) {
        return shoppingCartRepository.findById(id);
    }

    public Optional<ShoppingCart> findByUserId(String userId) {
        return shoppingCartRepository.findByUserId(userId);
    }

    public ShoppingCart save(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = shoppingCartDTO.generateCart();

        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUserId(shoppingCartDTO.getUserId());

        if (optionalShoppingCart.isPresent()) {
            shoppingCart = optionalShoppingCart.get();
            shoppingCart.setTotalPrice(new BigDecimal(0));
        }

        Set<CartItem> setCartItem = new HashSet<>();
        for (CartItemDTO cartItemDTO :
                shoppingCartDTO.getCartItemDTOSet()) {
            Optional<Product> optionalProduct = productRepository.findById(cartItemDTO.getProductId());
            if (!optionalProduct.isPresent()) {
                break;
            }

            if (cartItemDTO.getQuantity() <= 0){
                cartItemDTO.setStatus(-1);
            }

            Product product = optionalProduct.get();
            CartItem cartItem = CartItem.builder()
                    .id(new CartItemId(shoppingCart.getId(), product.getId()))
                    .productName(product.getName())
                    .productThumbnail(product.getThumbnail())
                    .quantity(cartItemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .shoppingCart(shoppingCart)
                    .status(cartItemDTO.getStatus())
                    .cartItemStatus(CartItemStatus.of(cartItemDTO.getStatus()))
                    .build();

            shoppingCart.addTotalPrice(cartItem); // add tổng giá bigdecimal
            setCartItem.add(cartItem);
        }
        shoppingCart.setCartItems(setCartItem);
        return shoppingCartRepository.save(shoppingCart);
    }

    public void deleteById(String id) {
        shoppingCartRepository.deleteById(id);
    }
}
