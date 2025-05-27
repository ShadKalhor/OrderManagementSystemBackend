package OrderManager.Entities;

import java.util.List;
import java.util.UUID;

public class OrderListAndOrder {


    private UUID id;
    private UUID orderId;
    private List<UUID> orderItemIds;

    public OrderListAndOrder(){
        id = UUID.randomUUID();

    }
    public OrderListAndOrder(UUID orderId, List<UUID> orderItemIds){
        id = UUID.randomUUID();
        this.orderId = orderId;
        this.orderItemIds = orderItemIds;
    }



    public List<UUID> getOrderItemIds() {
        return orderItemIds;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderItemIds(List<UUID> orderItemIds) {
        this.orderItemIds = orderItemIds;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
