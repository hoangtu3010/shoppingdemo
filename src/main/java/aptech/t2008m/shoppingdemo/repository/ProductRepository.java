package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
//    @Query(value = "SELECT * FROM products p INNER JOIN categories c ON p.category_id = c.id WHERE p.name LIKE %:keyword% OR c.name LIKE %:keyword%", countQuery = "SELECT count(*) FROM products", nativeQuery = true)
//    Page<Product> getPage(@Param("keyword") String keyword, Pageable pageable);
}
