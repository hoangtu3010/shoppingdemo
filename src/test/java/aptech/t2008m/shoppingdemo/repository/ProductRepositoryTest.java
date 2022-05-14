package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.ShoppingdemoApplication;
import aptech.t2008m.shoppingdemo.config.H2JpaConfig;
import aptech.t2008m.shoppingdemo.entity.Category;
import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;

import static aptech.t2008m.shoppingdemo.until.StringHelper.toSlug;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShoppingdemoApplication.class, H2JpaConfig.class})
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void Save() {
        Category category = new Category();
        category.setName("Category 01");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Bánh kẹo thơm ngon");
        product.setDescription("Description product 01");
        product.setThumbnail("Image product 01");
        product.setPrice(new BigDecimal(10));
        product.setCategoryId(1);
        product.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product);

        System.out.println(productRepository.findAll().size());

        Product product1 = productRepository.findAll().get(0);

        System.out.println(product1.toString());
    }
}