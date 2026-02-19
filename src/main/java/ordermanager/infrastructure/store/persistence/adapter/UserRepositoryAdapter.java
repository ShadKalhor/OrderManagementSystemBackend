package ordermanager.infrastructure.store.persistence.adapter;

import ordermanager.domain.port.out.UserPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserPersistencePort {

    private final SpringDataUserRepository userRepository;

    public UserRepositoryAdapter(SpringDataUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByPhone(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID userId) {

        userRepository.deleteById(userId);

    }
}
