package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import aptech.t2008m.shoppingdemo.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(generator = "productId")
    @GenericGenerator(name = "productId", parameters = {@Parameter(name = "prefix", value = "product"), @Parameter(name = "tableName", value = "Product")},strategy = "aptech.t2008m.shoppingdemo.generator.IdGenerator")
    private String id;
    private String name;
    private String thumbnail;
    private String description;
    private BigDecimal price;
    private String slug;
    @Enumerated(EnumType.ORDINAL)
    private ProductStatus status;
    @Column(insertable = false, updatable = false)
    private Integer categoryId;
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
//    @JsonManagedReference
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    private Set<OrderDetail> orderDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
