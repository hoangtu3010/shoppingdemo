package aptech.t2008m.shoppingdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CartItemId implements Serializable {
    @Column(name = "shoppingCartId")
    private String shoppingCartId;
    @Column(name = "productId")
    private String productId;
}
