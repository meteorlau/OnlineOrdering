package org.example.orderservice.repository;

import org.example.orderservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    // find by name
    Optional<Product> findByName(String name);

    // find by like name
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // update description
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.description = :description WHERE p.id = :productId")
    int updateDescription(@Param("productId") Long productId, @Param("description") String description);
}
