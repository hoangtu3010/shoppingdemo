package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.*;
import aptech.t2008m.shoppingdemo.entity.dto.OrderDTO;
import aptech.t2008m.shoppingdemo.entity.enums.OrderStatus;
import aptech.t2008m.shoppingdemo.repository.OrderRepository;
import aptech.t2008m.shoppingdemo.repository.ProductRepository;
import aptech.t2008m.shoppingdemo.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    public Order save(OrderDTO orderDTO) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUserId(orderDTO.getAccountId());
        if (!shoppingCartOptional.isPresent()){
            return null;
        }
        ShoppingCart existShoppingCart = shoppingCartOptional.get();

        Order order = orderDTO.generateOrder();

        Set<OrderDetail> orderDetails = order.getOrderDetails();
        if (orderDetails == null){
            orderDetails = new HashSet<>();
        }

        for (CartItem cartItem:
             existShoppingCart.getCartItems()) {
            Optional<Product> product = productRepository.findById(cartItem.getId().getProductId());

            if (!product.isPresent()){
                break;
            }

            Product existProduct = product.get();

            System.out.println(cartItem.getId().getProductId());

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

        return orderRepository.save(order);
    }

    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    public boolean addProductToOrder(Product product, Order order, Account account){
        try {
            BigDecimal totalPrice = new BigDecimal(0);

            Set<OrderDetail> orderDetails = order.getOrderDetails();
            if (orderDetails == null){
                orderDetails = new HashSet<>();
            }
            order.setId("");
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(new OrderDetailId(product.getId(), order.getId()));
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetail.setUnitPrice(product.getPrice());
            orderDetails.add(orderDetail);
            order.setAccountId(account.getId());
            order.setOrderDetails(orderDetails);

            for (OrderDetail od :
                    order.getOrderDetails()) {
                totalPrice.add(new BigDecimal(od.getQuantity()).multiply(od.getUnitPrice()));
            }
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
        }catch (Exception ex){
            return false;
        }
        return true;
    }
}
