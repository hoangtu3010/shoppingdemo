package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.base.BaseEntity;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String passwordHash;
    private Integer status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "identity_card_number")
    private IdentityCard identityCard;
}
