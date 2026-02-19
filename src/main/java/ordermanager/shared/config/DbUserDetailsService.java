package ordermanager.shared.config;

import ordermanager.adapter.out.persistence.UserRepositoryAdapter;
import ordermanager.infrastructure.store.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepositoryAdapter userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        System.out.println(">>> UserDetailsService input phone = [" + phone + "]");
        User u = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + phone));

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("Member"));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getPhone())
                .password(u.getPassword())
                .authorities(authorities)
                .accountLocked(false)
                .disabled(false)
                .build();
    }
}
