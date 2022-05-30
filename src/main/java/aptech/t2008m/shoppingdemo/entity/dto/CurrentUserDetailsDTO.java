package aptech.t2008m.shoppingdemo.entity.dto;

import aptech.t2008m.shoppingdemo.entity.Roles;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrentUserDetailsDTO {
    private String id;
    private String username;
    private Collection<Roles> roles;
}
