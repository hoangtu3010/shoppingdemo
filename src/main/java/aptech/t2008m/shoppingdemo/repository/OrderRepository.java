package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
