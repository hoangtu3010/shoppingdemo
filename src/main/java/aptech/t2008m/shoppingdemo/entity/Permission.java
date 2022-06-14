package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.enums.MethodsConstant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String url;
    @Enumerated(EnumType.ORDINAL)
    private MethodsConstant method;
    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference
    private Collection<Roles> roles;
}
