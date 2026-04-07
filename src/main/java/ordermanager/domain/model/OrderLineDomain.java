package ordermanager.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderLineDomain(UUID itemId, int qty, BigDecimal totalPrice) {}