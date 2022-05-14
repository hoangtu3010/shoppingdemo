package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.*;
import aptech.t2008m.shoppingdemo.repository.OrderRepository;
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
    private OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
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
