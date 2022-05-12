package aptech.t2008m.shoppingdemo.service;

import aptech.t2008m.shoppingdemo.entity.OrderDetail;
import aptech.t2008m.shoppingdemo.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public Optional<OrderDetail> findById(int id) {
        return orderDetailRepository.findById(id);
    }

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    public void deleteById(int id) {
        orderDetailRepository.deleteById(id);
    }
}
