package ordermanager.infrastructure.web.dto.user;

import java.util.UUID;
import ordermanager.domain.model.Utilities.*;

public record UserSummary(UUID id, String name, String phone, UserRoles role, Genders gender) {}
