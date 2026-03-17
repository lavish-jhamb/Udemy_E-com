package com.ecommerce.project.scripts;

import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.model.enums.Roles;
import com.ecommerce.project.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoles();

        if (userRepository.count() > 0) {
            return; // users already seeded
        }

        seedUsers();
    }

    /**
     * Ensures all roles exist in the DB.
     * Safe to run multiple times — skips roles that already exist.
     */
    private void seedRoles() {
        for (Roles roleEnum : Roles.values()) {
            if (roleRepository.findByRoles(roleEnum).isEmpty()) {
                Role role = new Role();
                role.setRoles(roleEnum);
                roleRepository.save(role);
            }
        }
    }

    private void seedUsers() {
        Role userRole = roleRepository.findByRoles(Roles.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found after seeding"));
        Role adminRole = roleRepository.findByRoles(Roles.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found after seeding"));

        User user = new User();
        user.setName("user");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(Set.of(userRole));

        User admin = new User();
        admin.setName("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(Set.of(adminRole));

        userRepository.saveAll(List.of(admin, user));
    }
}