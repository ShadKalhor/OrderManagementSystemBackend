package ordermanager.domain.dto.user;

import ordermanager.infrastructure.store.persistence.entity.Utilities.*;

import java.util.UUID;

public record UserResponse(UUID id, String name, String phone, UserRoles role, Genders gender) {}
