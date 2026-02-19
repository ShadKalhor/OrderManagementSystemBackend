package ordermanager.shared.dto.user;

import ordermanager.domain.model.Utilities.*;

import java.util.UUID;

public record UserResponse(UUID id, String name, String phone, UserRoles role, Genders gender) {}
