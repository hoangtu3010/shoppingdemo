package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Roles;
import aptech.t2008m.shoppingdemo.repository.RolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService {
    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Roles> findAll() {
        return rolesRepository.findAll();
    }

    public Optional<Roles> findById(String id) {
        return rolesRepository.findById(id);
    }

    public Roles save(Roles roles) {
        return rolesRepository.save(roles);
    }

    public List<Roles> findAllByNameIn(String[] roles) {
        return rolesRepository.findAllByNameIn(roles);
    }

    public void deleteById(String id) {
        rolesRepository.deleteById(id);
    }
}
