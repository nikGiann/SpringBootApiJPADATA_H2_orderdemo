package eu.acme.demo.web.dto;

import eu.acme.demo.domain.Order;
import java.util.List;

public class OrderDto extends OrderLiteDto {

    private List<OrderItemDto> orderItems;

    public OrderDto() {
    }
    
        
    public OrderDto(Order order){
        super(order);
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }
}
