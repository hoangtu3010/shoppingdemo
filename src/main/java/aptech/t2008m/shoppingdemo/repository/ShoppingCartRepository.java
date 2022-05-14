package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
