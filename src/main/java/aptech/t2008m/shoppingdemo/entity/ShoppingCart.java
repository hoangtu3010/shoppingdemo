package aptech.t2008m.shoppingdemo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(generator = "shoppingCartId")
    @GenericGenerator(name = "shoppingCartId", parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "shopping_cart"), @org.hibernate.annotations.Parameter(name = "tableName", value = "ShoppingCart")},strategy = "aptech.t2008m.shoppingdemo.generator.IdGenerator")
    private String id;
    private String userId;
    private BigDecimal totalPrice;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CartItem> cartItems;

    public void addTotalPrice(CartItem cartItem){
        if(this.totalPrice == null){
            this.totalPrice = new BigDecimal(0);
        }

        BigDecimal quantity = new BigDecimal(cartItem.getQuantity());
        this.totalPrice = this.totalPrice.add(cartItem.getUnitPrice().multiply(quantity));
    }
}
