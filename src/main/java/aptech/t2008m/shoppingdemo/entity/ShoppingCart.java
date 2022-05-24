package aptech.t2008m.shoppingdemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(generator = "shoppingCartId")
    @GenericGenerator(name = "shoppingCartId", parameters = {@Parameter(name = "prefix", value = "shopping_cart"), @Parameter(name = "tableName", value = "ShoppingCart")}, strategy = "aptech.t2008m.shoppingdemo.generator.IdGenerator")
    private String id;
    private String accountId;
    @OneToOne
    @JoinColumn(name = "accountId", insertable = false, updatable = false)
    @JsonBackReference
    private Account account;
    private BigDecimal totalPrice;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CartItem> cartItems;

    public void addTotalPrice(CartItem cartItem) {
        if (this.totalPrice == null) {
            this.totalPrice = new BigDecimal(0);
        }

        BigDecimal quantity = new BigDecimal(cartItem.getQuantity());
        if(cartItem.getStatus() == 1){
            this.totalPrice = this.totalPrice.add(cartItem.getUnitPrice().multiply(quantity));
        }
    }
}
