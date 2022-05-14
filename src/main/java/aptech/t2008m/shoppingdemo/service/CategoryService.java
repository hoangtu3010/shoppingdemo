package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.Category;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.repository.CategoryRepository;
import aptech.t2008m.shoppingdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
}
