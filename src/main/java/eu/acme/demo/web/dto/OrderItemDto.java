package eu.acme.demo.web.dto;

import eu.acme.demo.domain.OrderItem;
import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemDto {
    private UUID itemId;
    private int units;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    
    public OrderItemDto(){
    
    }
    
    public OrderItemDto(OrderItem orderItem){
        this.setItemId(orderItem.getId());
        this.setUnits(orderItem.getUnits());
        this.setUnitPrice(orderItem.getUnitPrice());
        this.setTotalPrice(orderItem.getTotalPrice());
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
