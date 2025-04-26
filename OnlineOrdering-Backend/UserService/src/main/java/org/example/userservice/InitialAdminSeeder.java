package org.example.userservice;

import org.example.userservice.entity.User;
import org.example.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialAdminSeeder implements CommandLineRunner {

    private final UserService userService;

    public InitialAdminSeeder(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        String initialUsername = "jDeppe";
        String rawPassword = "admin123";

        if (!userService.userExists(initialUsername)) {
            User admin = new User();
            admin.setUsername(initialUsername);
            admin.setPassword(rawPassword);
            userService.insertUser(admin, "ADMIN");
            System.out.println("Initial admin created.");
        } else {
            System.out.println("Admin account already exists. Skipping seeding.");
        }
    }
}
