package ordermanager.infrastructure.web.dto.item;

import java.util.UUID;

public record ItemSummary(UUID id, String name, String size) {}
