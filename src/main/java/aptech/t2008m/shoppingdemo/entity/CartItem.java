package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.enums.CartItemStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_item")
public class CartItem {
    @EmbeddedId
    private CartItemId id;
    @ManyToOne
    @MapsId("shoppingCartId")
    @JoinColumn(name = "shopping_cart_id")
    @JsonBackReference
    private ShoppingCart shoppingCart;
    private String productName;
    private String productThumbnail;
    private Integer quantity;
    private BigDecimal unitPrice;
    @Basic
    private int status;
    @Transient
    private CartItemStatus cartItemStatus;

    @PostLoad
    void fillTransient(){
        this.cartItemStatus = CartItemStatus.of(this.status);
    }
}
