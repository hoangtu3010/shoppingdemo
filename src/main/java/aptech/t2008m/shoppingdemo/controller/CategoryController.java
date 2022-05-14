package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import aptech.t2008m.shoppingdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Product> save(@RequestBody Product product) {
        product.setStatus(ProductStatus.ACTIVE);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product) {
        Optional<Product> optionalProduct = productService.findById(id);

        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Product existProduct = optionalProduct.get();
        existProduct.setName(product.getName());
        existProduct.setDescription(product.getDescription());
        existProduct.setPrice(product.getPrice());
        existProduct.setStatus(product.getStatus());

        return ResponseEntity.ok(productService.save(existProduct));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable String id) {
        Optional<Product> optionalProduct = productService.findById(id);

        if (!optionalProduct.isPresent()) {
            ResponseEntity.notFound();
        }

        return ResponseEntity.ok(optionalProduct.get());
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        Optional<Product> optionalProduct = productService.findById(id);

        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        Product existProduct = optionalProduct.get();
        existProduct.setStatus(ProductStatus.DELETED);
        productService.save(existProduct);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
