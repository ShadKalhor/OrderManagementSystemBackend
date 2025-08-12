package OrderManager.Adapter.out.Persistence;

import OrderManager.Application.Port.out.UserPersistencePort;
import OrderManager.Domain.Model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserPersistencePort {

    private SpringDataUserRepository userRepository;

    public UserRepositoryAdapter(SpringDataUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
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
