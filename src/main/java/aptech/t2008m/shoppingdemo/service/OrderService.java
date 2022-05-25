package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.*;
import aptech.t2008m.shoppingdemo.entity.dto.OrderDTO;
import aptech.t2008m.shoppingdemo.entity.enums.CartItemStatus;
import aptech.t2008m.shoppingdemo.entity.enums.OrderStatus;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteria;
import aptech.t2008m.shoppingdemo.entity.search.SearchCriteriaOperator;
import aptech.t2008m.shoppingdemo.repository.OrderRepository;
import aptech.t2008m.shoppingdemo.repository.ProductRepository;
import aptech.t2008m.shoppingdemo.repository.ShoppingCartRepository;
import aptech.t2008m.shoppingdemo.specifications.OrderSpecification;
import aptech.t2008m.shoppingdemo.until.CurrentUser;
import aptech.t2008m.shoppingdemo.until.DateTimeHelper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {
    private final ModelMapper modelMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;


    public OrderService(ShoppingCartRepository shoppingCartRepository, OrderRepository orderRepository, ProductRepository productRepository, AuthenticationService authenticationService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.authenticationService = authenticationService;
        this.modelMapper = new ModelMapper();
    }

    public Page<Order> getPage(Integer pageIndex, Integer pageSize, String startDate, String endDate,Integer status, String accountId, String userName, String productName) {
        Specification<Order> specification = Specification.where(null);

        if (status != null){
            OrderSpecification spec = new OrderSpecification(new SearchCriteria("status", SearchCriteriaOperator.EQUALS, status));
            specification = specification.and(spec);
        }

        if (!accountId.isEmpty()){
            OrderSpecification spec = new OrderSpecification(new SearchCriteria("accountId", SearchCriteriaOperator.EQUALS, accountId));
            specification = specification.and(spec);
        }

        if (!startDate.isEmpty()){
            OrderSpecification spec = new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS, DateTimeHelper.convertStringToLocalDateTime(startDate)));
            specification = specification.and(spec);
        }

        if (!endDate.isEmpty()){
            OrderSpecification spec = new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS, DateTimeHelper.convertStringToLocalDateTime(endDate)));
            specification = specification.and(spec);
        }

        if (!userName.isEmpty()){
            OrderSpecification spec = new OrderSpecification(new SearchCriteria("account", SearchCriteriaOperator.JOIN, userName));
            specification = specification.and(spec);
        }

        if (!productName.isEmpty()){
            OrderSpecification spec = new OrderSpecification(new SearchCriteria("product", SearchCriteriaOperator.JOIN, productName));
            specification = specification.and(spec);
        }

        return orderRepository.findAll(specification, PageRequest.of(pageIndex - 1, pageSize));
    }

    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    public Order save(OrderDTO orderDTO) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByAccount_UserName(CurrentUser.getCurrentUser().getName());
        if (!shoppingCartOptional.isPresent()){
            return null;
        }
        ShoppingCart existShoppingCart = shoppingCartOptional.get();

        Order order = modelMapper.map(orderDTO, Order.class);

        order.setAccountId(existShoppingCart.getAccountId());

        Set<OrderDetail> orderDetails = order.getOrderDetails();
        if (orderDetails == null){
            orderDetails = new HashSet<>();
        }

        for (CartItem cartItem:
             existShoppingCart.getCartItems()) {
            if (cartItem.getStatus() != CartItemStatus.ACTIVE.getValue()){
                continue;
            }

            Optional<Product> product = productRepository.findById(cartItem.getId().getProductId());

            if (!product.isPresent()){
                break;
            }

            Product existProduct = product.get();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(new OrderDetailId(cartItem.getId().getProductId(), order.getId()));
            orderDetail.setProduct(existProduct);
            orderDetail.setOrder(order);
            orderDetail.setUnitPrice(cartItem.getUnitPrice());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);
        order.setTotalPrice(existShoppingCart.getTotalPrice());
        order.setStatus(OrderStatus.WAITING);
        order.setCreatedBy(authenticationService.getCurrentUser().getUser().getId());
        order.setUpdatedBy(authenticationService.getCurrentUser().getUser().getId());

        existShoppingCart.setCartItems(new HashSet<>());
        existShoppingCart.setTotalPrice(new BigDecimal(0));
        shoppingCartRepository.save(existShoppingCart);

        return orderRepository.save(order);
    }

    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }
}
