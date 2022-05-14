package aptech.t2008m.shoppingdemo.repository;

import aptech.t2008m.shoppingdemo.ShoppingdemoApplication;
import aptech.t2008m.shoppingdemo.config.H2JpaConfig;
import aptech.t2008m.shoppingdemo.entity.*;
import aptech.t2008m.shoppingdemo.entity.dto.CartItemDTO;
import aptech.t2008m.shoppingdemo.entity.dto.ShoppingCartDTO;
import aptech.t2008m.shoppingdemo.entity.enums.ProductStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShoppingdemoApplication.class, H2JpaConfig.class})
@ActiveProfiles("test")
public class OrderRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Before // Set up
    public void before() throws Exception{
        Category category = new Category();
        category.setName("Category 01");
        categoryRepository.save(category);

        Product product1 = new Product();
        product1.setName("Product 01");
        product1.setDescription("Description product 01");
        product1.setThumbnail("Image product 01");
        product1.setPrice(new BigDecimal(10));
        product1.setCategoryId(1);
        product1.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product 02");
        product2.setDescription("Description product 02");
        product2.setThumbnail("Image product 02");
        product2.setPrice(new BigDecimal(20));
        product2.setCategoryId(1);
        product2.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product2);

        System.out.println(product1.toString());
        System.out.println(product2.toString());
    }

//    @Test
//    public void saveSimple(){
//        Order order = new Order();
//        order.setId("");
//        order.setAccountId(1);
//        order.setTotalPrice(new BigDecimal(0));
//        order.setShipName("Hoang Tu");
//        order.setShipAddress("HN");
//        order.setShipPhone("0123456789");
//        order.setStatus(1);
//
//        Set<OrderDetail> orderDetailSet = new HashSet<>();
//        // tạo product
//        Product product01 = productRepository.findById("product_1").get();
//        OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setId(new OrderDetailId(order.getId(), product01.getId()));
//        orderDetail.setOrder(order);
//        orderDetail.setProduct(product01);
//        orderDetail.setQuantity(3);
//        orderDetail.setUnitPrice(product01.getPrice());
//        orderDetailSet.add(orderDetail);
//        order.setTotalPrice(product01.getPrice().multiply(new BigDecimal(orderDetail.getQuantity())));
//
//        // tạo product
//        Product product02 = productRepository.findById("product_2").get();
//        OrderDetail orderDetail2 = new OrderDetail();
//        orderDetail2.setId(new OrderDetailId(order.getId(), product02.getId()));
//        orderDetail2.setOrder(order);
//        orderDetail2.setProduct(product02);
//        orderDetail2.setQuantity(5);
//        orderDetail2.setUnitPrice(product02.getPrice());
//        orderDetailSet.add(orderDetail2);
//        order.setTotalPrice(order.getTotalPrice().add(product02.getPrice().multiply(new BigDecimal(orderDetail2.getQuantity()))));
//
//        order.setOrderDetails(orderDetailSet);
//        orderRepository.save(order);
//
//        Order savedOrder = orderRepository.findAll().get(0);
//        System.out.println(savedOrder.getId());
//        System.out.println(savedOrder.getTotalPrice());
//        System.out.println(savedOrder.getOrderDetails().size());
//        for (OrderDetail od :
//                savedOrder.getOrderDetails()) {
//            System.out.println(od.getQuantity());
//            System.out.println(od.getUnitPrice());
//            System.out.println(od.getProduct().getName());
//            System.out.println(od.getProduct().getSlug());
//        }
//    }

    @Test
    public void realSaveOrder() {
        // ai là người đặt order.
        String userAccessToken = "";
        String userId = "A001";

        // thông tin giỏ hàng.
        CartItemDTO cartItemDTO1 = CartItemDTO.builder()
                .productId("product_1")
                .quantity(5)
                .build();
        CartItemDTO cartItemDTO2 = CartItemDTO.builder()
                .productId("product_2")
                .quantity(3)
                .build();

        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .userId(userId)
                .build();

        HashSet<CartItemDTO> cartItemDTOSet = new HashSet<>();
        cartItemDTOSet.add(cartItemDTO1);
        cartItemDTOSet.add(cartItemDTO2);
        shoppingCartDTO.setCartItemDTOSet(cartItemDTOSet);

        // chuyển DTO sang ShoppingCart và CartItem.
        // xử lý ở controller
        ShoppingCart shoppingCart = shoppingCartDTO.generateCart();
        Set<CartItem> setCartItem = new HashSet<>();
        for (CartItemDTO cartItemDTO :
                shoppingCartDTO.getCartItemDTOSet()) {
            Optional<Product> optionalProduct = productRepository.findById(cartItemDTO.getProductId());
            if(!optionalProduct.isPresent()){
                break;
            }
            Product product = optionalProduct.get();
            CartItem cartItem = CartItem.builder()
                    .id(new CartItemId(shoppingCart.getId(), product.getId()))
                    .productName(product.getName())
                    .productThumbnail(product.getThumbnail())
                    .quantity(cartItemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .shoppingCart(shoppingCart)
                    .build();

            shoppingCart.addTotalPrice(cartItem); // add tổng giá bigdecimal
            setCartItem.add(cartItem);
        }
        shoppingCart.setCartItems(setCartItem);
        shoppingCartRepository.save(shoppingCart);

        System.out.println(shoppingCartRepository.findAll().size());

        ShoppingCart shoppingCart1 = shoppingCartRepository.getById(shoppingCart.getId());
        System.out.println(shoppingCart1.getId());
    }
}