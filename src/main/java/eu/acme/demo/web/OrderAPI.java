package eu.acme.demo.web;

import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.OrderItem;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;

import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderItemDto;
import eu.acme.demo.web.dto.OrderLiteDto;
import eu.acme.demo.web.dto.OrderRequest;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders")
public class OrderAPI {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderAPI(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }
    
    @GetMapping
    public List<OrderLiteDto> fetchOrders() {
        //TODO: fetch all orders in DB
        List<Order> orders = orderRepository.findAll();
        List<OrderLiteDto> orderLiteDtos = orders!=null
                                            ? orders.stream()
                                                    .map(order -> new OrderLiteDto(order))
                                                    .collect(Collectors.toList())
                                            : new ArrayList<>();
        return orderLiteDtos;
    }

    @GetMapping("/{orderId}")
    public OrderDto fetchOrder(@PathVariable UUID orderId){
        //TODO: fetch specific order from DB
        // if order id not exists then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found");
        OrderDto orderDto = new OrderDto(order);
        List<OrderItem> orderItemList = orderItemRepository.findByOrderId(order.getId());
        orderDto.setOrderItems(orderItemList!=null
                                            ? orderItemList.stream()
                                                            .map(orderItem -> new OrderItemDto(orderItem))
                                                            .collect(Collectors.toList())
                                            : null);
        return orderDto;
    }
    

    
    @PostMapping
    public OrderDto submitOrder(@RequestBody OrderRequest orderRequest) {
        //TODO: submit a new order
        // if client reference code already exist then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        List<Order> orderList = orderRepository.findByClientReferenceCode(orderRequest.getClientReferenceCode());
        if(orderList!=null && orderList.size()>0)
            
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order already exists");

        Order order = new Order();
        order.setClientReferenceCode(orderRequest.getClientReferenceCode());
        order.setDescription(orderRequest.getDescription());
        order.setItemCount(orderRequest.getItemCount());
        order.setItemTotalAmount(orderRequest.getItemTotalAmount());
        order.setStatus(OrderStatus.valueOf(orderRequest.getStatus()));
        orderRepository.save(order);

        if(orderRequest.getOrderItems()!=null){
            List<OrderItem> orderItemList = new ArrayList<>();
            for(OrderItemDto orderItemDto: orderRequest.getOrderItems()){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setTotalPrice(orderItemDto.getTotalPrice());
                orderItem.setUnitPrice(orderItemDto.getUnitPrice());
                orderItem.setUnits(orderItemDto.getUnits());
                orderItemList.add(orderItem);
            }
            if(orderItemList.size()>0)
                orderItemRepository.saveAll(orderItemList);
        }

        OrderDto orderDto = new OrderDto(order);
        orderDto.setOrderItems(orderRequest.getOrderItems());

        return orderDto;
    }
    
}
