package OrderManager.Shared.Dto.UserDtos;

import OrderManager.Domain.Model.Utilities;

import java.util.UUID;

public record UserSummary(UUID id, String name, String phone, Utilities.UserRoles role, Utilities.Genders gender) {}
