package com.ecommerce.project.scripts;

import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.model.enums.Roles;
import com.ecommerce.project.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return; // already seeded
        }

        Role userRole = new Role();
        userRole.setRoles(Roles.ROLE_USER);

        Role adminRole = new Role();
        userRole.setRoles(Roles.ROLE_ADMIN);

        User user = new User();
        user.setName("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(Set.of(userRole));

        User admin = new User();
        admin.setName("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(Set.of(adminRole));

        userRepository.saveAll(List.of(admin, user));
    }
}