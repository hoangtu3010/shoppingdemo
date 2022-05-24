package aptech.t2008m.shoppingdemo.specifications;

import aptech.t2008m.shoppingdemo.entity.Account;
import aptech.t2008m.shoppingdemo.entity.Order;
import aptech.t2008m.shoppingdemo.entity.OrderDetail;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteria;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteriaOperator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderSpecification extends GenericSpecifications<Order> {


    public OrderSpecification(SearchCriteria criteria) {
        super(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch (getCriteria().getOperation()) {
            case JOIN:
                switch (getCriteria().getKey()) {
                    case "product":
                        Join<OrderDetail, Product> orderDetailProductJoin = root.join("orderDetails").join("product");
                        return criteriaBuilder.like(criteriaBuilder.lower(orderDetailProductJoin.get("name")), "%" + getCriteria().getValue() + "%");
                    case "account":
                        Join<Order, Account> accountJoin = root.join("account");
                        return criteriaBuilder.like(criteriaBuilder.lower(accountJoin.get("userName")), "%" + getCriteria().getValue() + "%");
                }
        }
        return super.toPredicate(root, query, criteriaBuilder);
    }
}
