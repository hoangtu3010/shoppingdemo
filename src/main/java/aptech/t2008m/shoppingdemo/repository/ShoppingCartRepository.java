package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String>, JpaSpecificationExecutor<ShoppingCart> {
    Optional<ShoppingCart> findByAccount_UserName(String userName);
}
