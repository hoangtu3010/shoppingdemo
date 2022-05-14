package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
