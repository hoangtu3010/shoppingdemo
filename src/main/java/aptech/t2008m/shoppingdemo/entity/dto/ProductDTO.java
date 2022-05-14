package aptech.t2008m.shoppingdemo.entity.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDTO {
    private String id;
    private String name;
    private String thumbnail;
    private String description;
    private BigDecimal price;
    private String slug;
    private String categoryName;
    private String status;
}
