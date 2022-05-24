package aptech.t2008m.shoppingdemo.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Entity
//@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(generator = "roleId")
    @GenericGenerator(name = "roleId", parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "role"), @org.hibernate.annotations.Parameter(name = "tableName", value = "Role")},strategy = "aptech.t2008m.shoppingdemo.generator.IdGenerator")
    private String id;
    private String name;
}
