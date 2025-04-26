package org.example.orderservice.repository;

import org.example.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // REMINDER: No need to declare methods like findAll(), save(), deleteById(), etc.

    // find by username
    Optional<User> findByUsername(String username);

    // read existence
    boolean existsByUsername(String username);

    // find Users by Role
    List<User> findByRoles_Id(Long roleId);

    // Optional: update isActive
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :userId")
    int deactivateUserByUserId(@Param("userId") Long userId);


    // update password but probably implemented in service with passwordEncoder instead
    // might need Spring security to complete the implementation
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    int updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);


    // (update) add a role
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_roles(user_id, role_id) VALUES(:userId, :roleId)", nativeQuery = true)
    int assignRoleByRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    // (update) delete a role
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_roles WHERE user_id = :userId AND role_id = :roleId", nativeQuery = true)
    int deleteRoleByRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}