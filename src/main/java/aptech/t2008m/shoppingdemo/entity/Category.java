package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.base.BaseEntity;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Enumerated(EnumType.ORDINAL)
    private ProductStatus status;
//    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
//    @JsonManagedReference
//    private Set<Product> products;
}
