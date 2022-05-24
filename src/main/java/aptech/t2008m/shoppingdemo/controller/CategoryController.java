package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Category;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import aptech.t2008m.shoppingdemo.service.CategoryService;
import aptech.t2008m.shoppingdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Category> save(@RequestBody Category category) {
        category.setStatus(ProductStatus.ACTIVE);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Category> update(@PathVariable Integer id, @RequestBody Category category) {
        Optional<Category> optionalCategory = categoryService.findById(id);

        if (!optionalCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Category existCategory = optionalCategory.get();
        existCategory.setName(category.getName());
        existCategory.setStatus(category.getStatus());

        return ResponseEntity.ok(categoryService.save(existCategory));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<Category> findById(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryService.findById(id);

        if (!optionalCategory.isPresent()) {
            ResponseEntity.notFound();
        }

        return ResponseEntity.ok(optionalCategory.get());
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryService.findById(id);

        if (!optionalCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        Category existCategory = optionalCategory.get();
        existCategory.setStatus(ProductStatus.DELETED);
        categoryService.save(existCategory);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
