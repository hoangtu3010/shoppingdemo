package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Permission;
import aptech.t2008m.shoppingdemo.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> findById(Integer id) {
        return permissionRepository.findById(id);
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public void deleteById(Integer id) {
        permissionRepository.deleteById(id);
    }
}
