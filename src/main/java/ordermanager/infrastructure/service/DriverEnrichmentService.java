package ordermanager.infrastructure.service;

import ordermanager.domain.model.UserDomain;
import ordermanager.infrastructure.mapper.UserMapper;

import ordermanager.infrastructure.web.dto.user.UserResponse;
import ordermanager.infrastructure.web.exception.ErrorStructureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DriverEnrichmentService {

    private final UserService userService;
    private final UserMapper userMapper;

    public DriverEnrichmentService(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public UserResponse getUserResponse(UUID accountId) {
        UserDomain user = userService.GetUserById(accountId).getOrElseThrow(ErrorStructureException::new);
        return userMapper.domainToResponse(user);
    }
}