package eu.acme.demo;


import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
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
        o.setItemCount(10);
        o.setItemTotalAmount(BigDecimal.valueOf(100.23));
        orderRepository.save(o);

        Assert.isTrue(orderRepository.findById(o.getId()).isPresent(), "order not found");
        Assert.isTrue(!orderRepository.findById(UUID.randomUUID()).isPresent(), "non existing order found");

        //TODO: add tests for order items
    }

}
