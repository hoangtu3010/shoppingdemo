package aptech.t2008m.shoppingdemo.entity.dto;

import aptech.t2008m.shoppingdemo.entity.Order;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDTO {
    private String accountId;
    private String shipName;
    private String shipAddress;
    private String shipPhone;
    private String shipNote;

    public Order generateOrder() {
        return Order.builder()
                .accountId(this.accountId)
                .shipName(this.shipName)
                .shipAddress(this.shipAddress)
                .shipPhone(this.shipPhone)
                .shipNote(this.shipNote)
                .build();
    }
}
