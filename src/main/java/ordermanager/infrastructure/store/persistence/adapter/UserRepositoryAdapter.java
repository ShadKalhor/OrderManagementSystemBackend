package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.domain.port.out.UserPersistencePort;
import ordermanager.infrastructure.mapper.UserMapper;
import ordermanager.infrastructure.store.persistence.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserPersistencePort {

    private final SpringDataUserRepository userRepository;


    public UserRepositoryAdapter(SpringDataUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Option<User> findById(UUID userId) {
                return userRepository.findOptionById(userId);
    }

    @Override
    public Option<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Option<User> findByPhone(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber);
    }

    @Override
    public Option<User> save(User user) {
        return Option.of(userRepository.save(user));
    }

    @Override
    public void deleteById(UUID userId) {userRepository.deleteById(userId);}
}
