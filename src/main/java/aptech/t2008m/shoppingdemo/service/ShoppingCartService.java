package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.ShoppingCart;
import aptech.t2008m.shoppingdemo.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public List<ShoppingCart> findAll() {
        return shoppingCartRepository.findAll();
    }

    public Optional<ShoppingCart> findById(String id) {
        return shoppingCartRepository.findById(id);
    }

    public Optional<ShoppingCart> findByUserId(String userId) {
        return shoppingCartRepository.findByUserId(userId);
    }

    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    public void deleteById(String id) {
        shoppingCartRepository.deleteById(id);
    }
}
