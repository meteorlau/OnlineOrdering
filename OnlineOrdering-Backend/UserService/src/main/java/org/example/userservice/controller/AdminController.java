package org.example.userservice.controller;

import org.example.userservice.dto.OnlineOrderDTO;
import org.example.userservice.dto.PasswordChangeRequest;
import org.example.userservice.entity.*;
import org.example.userservice.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // ----- account management -----
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request,
                                                 Principal principal) {
        userService.changePassword(principal.getName(), request);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @GetMapping("/customers")
    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok(userService.findUsersWithRole("USER"));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/staffs")
    public ResponseEntity<List<User>> findAllAdmins() {
        return ResponseEntity.ok(userService.findUsersWithRole("ADMIN"));
    }

    // ----- user management -----
//    @PostMapping("/create-user")
//    public ResponseEntity<String> createUser(@RequestBody User user) {
//        // Admins can create a new user
//        userService.insertUser(user, "USER"); // Assign default role "USER"
//        return ResponseEntity.ok("User created successfully!");
//    }

    @PostMapping("/users/{username}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable String username) {
        // Admins can delete a user
        userService.softDeleteUser(username);
        return ResponseEntity.ok("User deactivated successfully!");
    }
}

