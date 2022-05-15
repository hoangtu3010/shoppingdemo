package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getPage(String keyword, String sortPrice, int pageIndex, int pageSize) {
        if (pageIndex <= 0){
            pageIndex = 1;
        }

        if (pageSize < 0){
            pageSize = 5;
        }

        if (pageSize == 0){
            return productRepository.getPage(keyword, null);
        }

        if (sortPrice.equals("DESC")){
            return productRepository.getPage(keyword, PageRequest.of(pageIndex - 1, pageSize, Sort.by("price").descending()));
        }else if(sortPrice.equals("ASC")) {
            return productRepository.getPage(keyword, PageRequest.of(pageIndex - 1, pageSize, Sort.by("price").ascending()));
        }

        return productRepository.getPage(keyword, PageRequest.of(pageIndex - 1, pageSize));
    }

//    public List<Product> getListSearch(String keyword){
//        return productRepository.search(keyword);
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
