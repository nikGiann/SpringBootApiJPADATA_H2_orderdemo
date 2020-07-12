package eu.acme.demo;


import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.OrderItem;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import eu.acme.demo.web.dto.OrderItemDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class OrderDataTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void testCreateOrder() {
        Order o = new Order();
        o.setStatus(OrderStatus.SUBMITTED);
        o.setClientReferenceCode("ORDER-1");
        o.setDescription("first order");
        o.setItemCount(9);
        o.setItemTotalAmount(BigDecimal.valueOf(100.23));
        orderRepository.save(o);

        Assert.isTrue(orderRepository.findById(o.getId()).isPresent(), "order not found");
        Assert.isTrue(!orderRepository.findById(UUID.randomUUID()).isPresent(), "non existing order found");

        //TODO: add tests for order items
        
//        List<OrderItem> list = new ArrayList();
//        OrderItem orderItemFirst = new OrderItem();
//        orderItemFirst.setOrder(o);
//        orderItemFirst.setTotalPrice(BigDecimal.valueOf(80.00));
//        orderItemFirst.setUnitPrice(BigDecimal.valueOf(10.00));
//        orderItemFirst.setUnits(8);
//        list.add(orderItemFirst);
//        
//        
//        OrderItem orderItemSecond = new OrderItem();
//        orderItemSecond.setOrder(o);
//        orderItemSecond.setTotalPrice(BigDecimal.valueOf(20.23));
//        orderItemSecond.setUnitPrice(BigDecimal.valueOf(20.23));
//        orderItemSecond.setUnits(1);
//        list.add(orderItemSecond);
//        
//        Assert.isTrue(orderItemRepository.findById(o.getId()).isPresent(), "order item not found");
//        Assert.isTrue(!orderItemRepository.findById(UUID.randomUUID()).isPresent(), "non existing order item found");
//        
        }

}
