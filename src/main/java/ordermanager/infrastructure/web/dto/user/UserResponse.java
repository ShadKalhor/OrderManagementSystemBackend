package ordermanager.infrastructure.web.dto.user;

import ordermanager.domain.model.Genders;
import ordermanager.domain.model.UserRoles;

import java.util.UUID;

public record UserResponse(UUID id, String name, String phone, UserRoles role, Genders gender) {}
