package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteria;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteriaOperator;
import aptech.t2008m.shoppingdemo.repository.ProductRepository;
import aptech.t2008m.shoppingdemo.specifications.GenericSpecifications;
import aptech.t2008m.shoppingdemo.specifications.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> getPage(String keyword, Integer categoryId, String sortPrice, int pageIndex, int pageSize) {
        Specification<Product> specification = Specification.where(null);
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize);

        if (keyword != null){
            ProductSpecification spec = new ProductSpecification(new SearchCriteria("name", SearchCriteriaOperator.LIKE, keyword));
            specification = specification.and(spec);
        }

        if (categoryId > 0){
            ProductSpecification spec = new ProductSpecification(new SearchCriteria("categoryId", SearchCriteriaOperator.EQUALS, categoryId));
            specification = specification.and(spec);
        }

        if (sortPrice.equals("DESC")) {
           pageRequest = PageRequest.of(pageIndex - 1, pageSize, Sort.by("price").descending());
        } else if (sortPrice.equals("ASC")) {
            pageRequest = PageRequest.of(pageIndex - 1, pageSize, Sort.by("price").ascending());
        }

        return productRepository.findAll(specification, pageRequest);
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product account) {
        return productRepository.save(account);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }
}
