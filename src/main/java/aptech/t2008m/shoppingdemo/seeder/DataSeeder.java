package aptech.t2008m.shoppingdemo.seeder;

import aptech.t2008m.shoppingdemo.entity.*;
import aptech.t2008m.shoppingdemo.entity.enums.*;
import aptech.t2008m.shoppingdemo.repository.*;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {
    private final Faker faker = new Faker();
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final RolesRepository rolesRepository;

    public DataSeeder(CategoryRepository categoryRepository, OrderRepository orderRepository, ProductRepository productRepository, AccountRepository accountRepository, ShoppingCartRepository shoppingCartRepository, RolesRepository rolesRepository) {
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.rolesRepository = rolesRepository;
    }

    public static List<String> fakeImages = new ArrayList<>();

    @Override
    public void run(String... args) throws Exception {
        fakeImages.add("https://photographer.vn/wp-content/uploads/2020/09/chupanhdouongsangtrong1.jpg");
        fakeImages.add("http://rivalmedia.vn/wp-content/uploads/2021/06/chup-anh-san-pham-1.jpg");
        fakeImages.add("https://lavenderstudio.com.vn/wp-content/uploads/2017/03/chup-san-pham-uy-tin.jpg");
        fakeImages.add("http://rivalmedia.vn/wp-content/uploads/2021/06/chup-anh-san-pham-2.jpg");
        fakeImages.add("https://d1j8r0kxyu9tj8.cloudfront.net/images/1565661304KeDpTYhsF3F95Xt.jpg");
        fakeImages.add("https://margram.vn/files/chup-anh-san-pham-61.jpg");
        fakeImages.add("https://images.squarespace-cdn.com/content/v1/53883795e4b016c956b8d243/1548761631280-IWOZDVZXHRQYRIGYG365/27072809_2107874492572797_5078792318071986073_n.jpg");
        fakeImages.add("https://htmediagroup.vn/wp-content/uploads/2021/07/anh-my-pham-3-min.jpg");
        fakeImages.add("https://htmediagroup.vn/wp-content/uploads/2021/07/anh-my-pham-2-min.jpg");

        seedCategory();
        seedProduct();
        seedRoles();
        seedAccount();
        seedShoppingCart();
        seedOrder();
    }

    private void seedRoles() {
        if (rolesRepository.findAll().isEmpty()){
            List<Roles> roles = new ArrayList<>();

            Roles role1 = new Roles();
            role1.setName("user");

            Roles role2 = new Roles();
            role2.setName("admin");

            Roles role3 = new Roles();
            role3.setName("moderator");

            roles.add(role1);
            roles.add(role2);
            roles.add(role3);

            rolesRepository.saveAll(roles);
        }
    }

    private void seedAccount() {
        if (accountRepository.findAll().isEmpty()) {
            List<Account> accounts = new ArrayList<>();
            List<Roles> rolesList = rolesRepository.findAll();

            for (int i = 0; i < 5; i++) {
                Set<Roles> roles = new HashSet<>();
                int rolesNumber = faker.number().numberBetween(1, rolesList.size());
                for (int j = 0; j < rolesNumber; j++){
                    roles.add(rolesList.get(faker.number().numberBetween(0, rolesList.size() - 1)));
                }

                Account account = new Account();
                account.setUserName(faker.name().username());
                account.setRoles(roles);
                account.setStatus(AccountStatus.ACTIVE);
                accounts.add(account);
            }

            accountRepository.saveAll(accounts);
        }
    }

    private void seedCategory() {
        if (categoryRepository.findAll().isEmpty()) {
            List<Category> categories = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                Category category = new Category();
                category.setName(faker.name().title());
                category.setStatus(ProductStatus.ACTIVE);
                categories.add(category);
            }

            categoryRepository.saveAll(categories);
        }
    }

    private void seedProduct() {
        if (productRepository.findAll().isEmpty()) {
            List<Product> products = new ArrayList<>();
            List<Category> categories = categoryRepository.findAll();

            for (int i = 0; i < 100; i++) {
                Product product = new Product();
                product.setName(faker.name().title());
                product.setThumbnail(fakeImages.get(faker.number().numberBetween(0, fakeImages.size()-1)));
                product.setDescription(faker.lorem().sentence());
                product.setPrice(new BigDecimal(faker.number().numberBetween(1, 999)));
                product.setCategoryId(categories.get(faker.number().numberBetween(0, categories.size() - 1)).getId());
                product.setStatus(ProductStatus.ACTIVE);
                products.add(product);
            }

            productRepository.saveAll(products);
        }
    }

    private void seedShoppingCart() {
        if (shoppingCartRepository.findAll().isEmpty()) {
            List<Account> accounts = accountRepository.findAll();
            List<Product> products = productRepository.findAll();

            List<ShoppingCart> shoppingCarts = new ArrayList<>();
            HashSet<String> existingAccountId = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                Account randomAccount = accounts.get(faker.number().numberBetween(0, accounts.size() - 1));

                if (existingAccountId.contains(randomAccount.getId())) {
                    continue;
                }

                HashSet<String> existingProductId = new HashSet<>();

                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setAccountId(randomAccount.getId());

                Set<CartItem> cartItems = new HashSet<>();
                int randomCartItem = faker.number().numberBetween(1, 5);
                for (int j = 0; j < randomCartItem; j++) {
                    Product randomProduct = products.get(faker.number().numberBetween(0, accounts.size() - 1));

                    if (existingProductId.contains(randomProduct.getId())) {
                        continue;
                    }

                    CartItem cartItem = CartItem.builder()
                            .id(new CartItemId(shoppingCart.getId(), randomProduct.getId()))
                            .productName(randomProduct.getName())
                            .productThumbnail(randomProduct.getThumbnail())
                            .quantity(faker.number().numberBetween(1, 3))
                            .unitPrice(randomProduct.getPrice())
                            .shoppingCart(shoppingCart)
                            .status(CartItemStatus.ACTIVE.getValue())
                            .cartItemStatus(CartItemStatus.ACTIVE)
                            .build();
                    cartItems.add(cartItem);
                    shoppingCart.addTotalPrice(cartItem);
                    existingProductId.add(randomProduct.getId());
                }
                shoppingCart.setCartItems(cartItems);
                shoppingCarts.add(shoppingCart);
                existingAccountId.add(randomAccount.getId());
            }

            shoppingCartRepository.saveAll(shoppingCarts);
        }
    }

    private void seedOrder() {
        if (orderRepository.findAll().isEmpty()) {
            List<Account> accounts = accountRepository.findAll();
            List<Product> products = productRepository.findAll();

            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Account randomAccount = accounts.get(faker.number().numberBetween(0, accounts.size() - 1));

                Order order = new Order();
                order.setId("");
                order.setAccountId(randomAccount.getId());
                order.setShipName(faker.name().name());
                order.setShipAddress(faker.address().city());
                order.setShipPhone(faker.phoneNumber().cellPhone());
                order.setShipNote(faker.lorem().sentence());

                Set<OrderDetail> orderDetails = new HashSet<>();
                HashSet<String> existingProductId = new HashSet<>();

                int randomOrderDetails = faker.number().numberBetween(1, 5);
                for (int j = 0; j < randomOrderDetails; j++) {
                    Product randomProduct = products.get(faker.number().numberBetween(0, accounts.size() - 1));

                    if (existingProductId.contains(randomProduct.getId())) {
                        continue;
                    }

                    OrderDetail orderDetail = OrderDetail.builder()
                            .id(new OrderDetailId(randomProduct.getId(), order.getId()))
                            .order(order)
                            .product(randomProduct)
                            .quantity(faker.number().numberBetween(1, 3))
                            .unitPrice(randomProduct.getPrice())
                            .build();
                    orderDetails.add(orderDetail);
                    order.addTotalPrice(orderDetail);
                    existingProductId.add(randomProduct.getId());
                }
                order.setStatus(OrderStatus.WAITING);
                order.setOrderDetails(orderDetails);
                orders.add(order);
            }

            orderRepository.saveAll(orders);
        }
    }
}
