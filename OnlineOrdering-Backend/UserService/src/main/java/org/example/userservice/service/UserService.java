package org.example.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.example.userservice.dto.PasswordChangeRequest;
import org.example.userservice.repository.RoleRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----- Create -----
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public User insertUser(User user, String roleName) {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("User username and password must not be null.");
        }

        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("User already exists with this username.");
        }

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Get role (default role is USER, admin role is optional)
        Role role = getRoleByRoleName(roleName != null ? roleName : "USER");  // default to "USER" role if no role is provided

        // Save the user and assign the role
        user = userRepository.save(user);
        userRepository.assignRoleByRoleId(user.getId(), role.getId());

        return user;
    }


    // ----- Read -----
    public boolean isAdmin(String username) {
        User user = getUserByUsername(username);
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
    }

    public Role getRoleByRoleName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    public List<User> findUsersWithRole(String roleName) {
        Role role = getRoleByRoleName(roleName);
        return userRepository.findByRoles_Id(role.getId());
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

    // ----- Update -----

    @Transactional
    public void changePassword(String username, PasswordChangeRequest request) {
        String oldPassword = request.getPassword();
        User user = getActiveAuthenticatedUser(username, oldPassword);

        String newPassword = request.getNewPassword();
        String reEnteredPassword = request.getReEnteredPassword();
        if (newPassword.isEmpty() || reEnteredPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty.");
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Need a new password.");
        }
        if (!newPassword.equals(reEnteredPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        int updated = userRepository.updatePassword(user.getId(), passwordEncoder.encode(newPassword));
        if (updated == 0) {
            throw new IllegalStateException("Failed to change password.");
        }
    }

//    @Transactional
//    public void addRoleToUser(String username, String roleName) {
//        User user = getActiveUser(username, false);
//        Role role = getRoleByRoleName(roleName);
//
//        int inserted = userRepository.assignRoleByRoleId(user.getId(), role.getId());
//        if (inserted == 0) {
//            throw new IllegalStateException("Failed to assign role.");
//        }
//    }
//
//    @Transactional
//    public void removeRoleFromUser(String username, String roleName) {
//        // does not require account to be active!
//        User user = getUserByUsername(username);
//        Role role = getRoleByRoleName(roleName);
//
//        int deleted = userRepository.deleteRoleByRoleId(user.getId(), role.getId());
//        if (deleted == 0) {
//            throw new IllegalStateException("Failed to remove role.");
//        }
//    }

    // Client Side method
    @Transactional
    public void softDeleteUser(String username) {
        User user = getActiveUser(username, false);

        int updated = userRepository.deactivateUserByUserId(user.getId());
        if (updated == 0) {
            throw new IllegalStateException("Failed to deactivate user.");
        }
    }
}

