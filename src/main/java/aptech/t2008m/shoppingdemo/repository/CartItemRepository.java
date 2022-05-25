package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.entity.CartItem;
import aptech.t2008m.shoppingdemo.entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
