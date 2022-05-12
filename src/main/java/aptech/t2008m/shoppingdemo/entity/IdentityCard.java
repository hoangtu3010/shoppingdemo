package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "identity_card")
public class IdentityCard extends BaseEntity {
    @Id
    private String number;
    private String fullName;
    private Timestamp birthDay;
    private Integer gender;
    private String address;
    private String description;
    private String createdBy;
    private int status;
//    @OneToOne(mappedBy = "identityCard")
//    private Account account;
}
