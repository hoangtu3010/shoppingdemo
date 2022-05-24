package aptech.t2008m.shoppingdemo.entity.search;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {
    private String key;
    private SearchCriteriaOperator operation;
    private Object value;
}
