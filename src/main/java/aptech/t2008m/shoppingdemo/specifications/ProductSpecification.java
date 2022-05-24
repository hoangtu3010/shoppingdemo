package aptech.t2008m.shoppingdemo.specifications;

import aptech.t2008m.shoppingdemo.entity.Category;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteria;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteriaOperator;
import lombok.*;

import javax.persistence.criteria.*;

@Getter
@Setter
public class ProductSpecification extends GenericSpecifications<Product> {
    public ProductSpecification (SearchCriteria criteria){
        super(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (getCriteria().getOperation() == SearchCriteriaOperator.JOIN) {
            Join<Product, Category> productCategoryJoin = root.join("category");
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(productCategoryJoin.get(getCriteria().getKey())), "%" + getCriteria().getValue() + "%")
            );
        }

        return super.toPredicate(root, query, criteriaBuilder);
    }
}
