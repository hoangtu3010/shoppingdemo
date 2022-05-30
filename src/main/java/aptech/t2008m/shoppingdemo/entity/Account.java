package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.base.BaseEntity;
import aptech.t2008m.shoppingdemo.entity.enums.AccountStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(generator = "accountId")
    @GenericGenerator(name = "accountId", parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "account"), @org.hibernate.annotations.Parameter(name = "tableName", value = "Account")},strategy = "aptech.t2008m.shoppingdemo.generator.IdGenerator")
    private String id;
    private String userName;
    private String passwordHash;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Collection<Roles> roles;
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus status;
}
