package ordermanager.infrastructure.web.dto.user;

import java.util.UUID;
import ordermanager.infrastructure.store.persistence.entity.Utilities.*;

public record UserSummary(UUID id, String name, String phone, UserRoles role, Genders gender) {}
