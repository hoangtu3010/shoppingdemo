package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

//    public Page<Product> getPage(int page, int limit){
//        return productRepository.getPage(PageRequest.of(page, limit));
//    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product account) {
        return productRepository.save(account);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }
}
