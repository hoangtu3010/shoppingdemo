package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.base.BaseEntity;
import aptech.t2008m.shoppingdemo.entity.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(generator = "orderId")
    @GenericGenerator(name = "orderId", parameters = {@Parameter(name = "prefix", value = "order"), @Parameter(name = "tableName", value = "Order")}, strategy = "aptech.t2008m.shoppingdemo.generator.IdGenerator")
    private String id;
    private String accountId;
    @OneToOne
    @JoinColumn(name = "accountId", insertable = false, updatable = false)
    private Account account;
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;
    private BigDecimal totalPrice;
    private String shipName;
    private String shipAddress;
    private String shipPhone;
    private String shipNote;
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    public void addTotalPrice(OrderDetail orderDetail) {
        if (this.totalPrice == null) {
            this.totalPrice = new BigDecimal(0);
        }

        BigDecimal quantity = new BigDecimal(orderDetail.getQuantity());
        this.totalPrice = this.totalPrice.add(orderDetail.getUnitPrice().multiply(quantity));
    }
}
