package aptech.t2008m.shoppingdemo.entity;

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
    private String method;
    @ManyToMany(targetEntity = Roles.class, mappedBy = "permissions", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("permissions")
    private Collection<Roles> roles;
}
