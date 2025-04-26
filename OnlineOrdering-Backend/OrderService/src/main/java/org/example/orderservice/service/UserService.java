package org.example.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.orderservice.entity.User;
import org.example.orderservice.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----- Create -----
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // ----- Read -----
    public boolean isAdmin(String username) {
        User user = getUserByUsername(username);
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private User getActiveAuthenticatedUser(String username, String password) {
        User user = getActiveUser(username, true);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        return user;
    }

    private User getActiveUser(String username, boolean loginFlag) {
        String nameErrMsg = loginFlag ? "Invalid username or password." : "User not found.";
        String isActiveErrMsg = loginFlag ? "Invalid username or password." : "Account was deactivated.";

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(nameErrMsg)); // user not found

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new DisabledException(isActiveErrMsg); // account is deactivated
        }
        return user;
    }
}

