package aptech.t2008m.shoppingdemo.entity.base;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class FilterParams {
    public String keyword;
    private Integer pageIndex;
    private Integer pageSize;
}
