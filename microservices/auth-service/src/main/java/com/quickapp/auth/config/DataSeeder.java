package com.quickapp.auth.config;

import com.quickapp.auth.model.Permission;
import com.quickapp.auth.model.Role;
import com.quickapp.auth.model.User;
import com.quickapp.auth.repository.RoleRepository;
import com.quickapp.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            logger.info("Seeding default users and roles");

            Role adminRole = new Role("administrator", "Default administrator");
            adminRole.setPermissions(new HashSet<>(Arrays.asList(Permission.getAllPermissionValues())));
            adminRole = roleRepository.save(adminRole);

            Role userRole = new Role("user", "Default user");
            userRole.setPermissions(new HashSet<>());
            userRole = roleRepository.save(userRole);

            User admin = new User();
            admin.setUserName("admin");
            admin.setFullName("Inbuilt Administrator");
            admin.setEmail("admin@ebenmonney.com");
            admin.setPhoneNumber("+1 (123) 000-0000");
            admin.setPasswordHash(passwordEncoder.encode("tempP@ss123"));
            admin.setIsEnabled(true);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);

            User user = new User();
            user.setUserName("user");
            user.setFullName("Inbuilt Standard User");
            user.setEmail("user@ebenmonney.com");
            user.setPhoneNumber("+1 (123) 000-0001");
            user.setPasswordHash(passwordEncoder.encode("tempP@ss123"));
            user.setIsEnabled(true);
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

            logger.info("Default users and roles seeded successfully");
        }
    }
}
