package aptech.t2008m.shoppingdemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(generator = "roleId")
    @GenericGenerator(name = "roleId", parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "role"), @org.hibernate.annotations.Parameter(name = "tableName", value = "Roles")},strategy = "aptech.t2008m.shoppingdemo.generator.IdGenerator")
    private String id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<Account> accounts;
}
