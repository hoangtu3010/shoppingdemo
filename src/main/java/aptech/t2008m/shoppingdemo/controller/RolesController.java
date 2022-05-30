package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Permission;
import aptech.t2008m.shoppingdemo.entity.Roles;
import aptech.t2008m.shoppingdemo.service.PermissionService;
import aptech.t2008m.shoppingdemo.service.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/roles")
public class RolesController {
    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Roles>> findAll() {
        return ResponseEntity.ok(rolesService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Roles> save(@RequestBody Roles roles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolesService.save(roles));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Roles> update(@PathVariable String id, @RequestBody Roles roles) {
        Optional<Roles> optionalRoles = rolesService.findById(id);

        if (!optionalRoles.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Roles existRoles = optionalRoles.get();
        existRoles.setName(roles.getName());

        return ResponseEntity.ok(rolesService.save(existRoles));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<Roles> findById(@PathVariable String id) {
        Optional<Roles> optionalRoles = rolesService.findById(id);

        if (!optionalRoles.isPresent()) {
            ResponseEntity.notFound();
        }

        return ResponseEntity.ok(optionalRoles.get());
    }
}
