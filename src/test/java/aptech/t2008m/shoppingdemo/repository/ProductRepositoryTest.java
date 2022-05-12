package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.ShoppingdemoApplication;
import aptech.t2008m.shoppingdemo.config.H2JpaConfig;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShoppingdemoApplication.class, H2JpaConfig.class})
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void Save() {
        Product product = new Product();
        product.setName("Product 02");
        product.setDescription("Description product 02");
        product.setThumbnail("Image product 02");
        product.setPrice(new BigDecimal(10));
        product.setSlug("product-02");
        product.setCategoryId(1);
        product.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product);

        System.out.println(productRepository.findAll().size());

        Product product1 = productRepository.findAll().get(0);

        System.out.println(product1.toString());
    }
}