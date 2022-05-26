package aptech.t2008m.shoppingdemo.entity;

import aptech.t2008m.shoppingdemo.entity.dto.CurrentUserDetailsDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrentUserDetails {
    private CurrentUserDetailsDTO user;
}
