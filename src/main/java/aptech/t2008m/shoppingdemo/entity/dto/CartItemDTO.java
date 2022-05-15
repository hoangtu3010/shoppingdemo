package aptech.t2008m.shoppingdemo.entity.dto;

import lombok.*;

import javax.persistence.PrePersist;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CartItemDTO {
    private String productId;
    private Integer quantity;
    private Integer status = 1;
}
