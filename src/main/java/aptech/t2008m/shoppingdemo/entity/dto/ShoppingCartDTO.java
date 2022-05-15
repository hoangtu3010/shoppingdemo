package aptech.t2008m.shoppingdemo.entity.dto;

import aptech.t2008m.shoppingdemo.entity.ShoppingCart;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ShoppingCartDTO {
    private String id;
    private String userId;
    private BigDecimal totalPrice;
    private Set<CartItemDTO> cartItemDTOSet;

    public ShoppingCart generateCart() {
        return ShoppingCart.builder()
                .userId(this.userId)
                .build();
    }
}
