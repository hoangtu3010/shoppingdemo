package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
    private Integer status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "identity_card_number")
    private IdentityCard identityCard;
}
