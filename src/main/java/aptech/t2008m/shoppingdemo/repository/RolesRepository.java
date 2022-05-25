package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, String> {
}
