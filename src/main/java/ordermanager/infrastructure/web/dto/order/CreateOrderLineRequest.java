package ordermanager.infrastructure.web.dto.order;


import java.util.UUID;

public record CreateOrderLineRequest(UUID itemId, int qty) {}
