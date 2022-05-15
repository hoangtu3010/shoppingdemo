package aptech.t2008m.shoppingdemo.controller;

import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import aptech.t2008m.shoppingdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<Product>> getPage(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(productService.getPage(keyword, sort, pageIndex, pageSize));
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/search")
//    public ResponseEntity<List<Product>> getListSearch(@RequestParam(defaultValue = "") String keyword) {
//        return ResponseEntity.ok(productService.getListSearch(keyword));
//    }

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


        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.ACTIVE);
        }

        existProduct.setName(product.getName());
        existProduct.setThumbnail(product.getThumbnail());
        existProduct.setDescription(product.getDescription());
        existProduct.setPrice(product.getPrice());
        existProduct.setSlug(product.getSlug());
        existProduct.setStatus(product.getStatus());
        existProduct.setCategoryId(product.getCategoryId());

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
