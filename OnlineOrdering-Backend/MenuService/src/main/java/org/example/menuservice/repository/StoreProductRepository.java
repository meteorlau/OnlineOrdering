package org.example.menuservice.repository;

import org.example.menuservice.entity.StoreProduct;
import org.example.menuservice.entity.StoreProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
    Optional<StoreProduct> findById(StoreProductId storeProductId);

    boolean existsById(StoreProductId storeProductId);

    List<StoreProduct> findByStore_Id(@Param("storeId")Long storeId);

    List<StoreProduct> findByProduct_Id(@Param("productId")Long productId);

    // [ADMIN] Update stock number for a specific store-product
    @Modifying
    @Transactional
    @Query("UPDATE StoreProduct sp SET sp.stock = :newStock WHERE sp.id = :storeProductId")
    int updateStock(@Param("storeProductId") StoreProductId storeProductId, @Param("newStock") int newStock);

    // [ADMIN] Update unit price for a specific store-product
    @Modifying
    @Transactional
    @Query("UPDATE StoreProduct sp SET sp.unitPrice = :newPrice WHERE sp.id = :storeProductId")
    int updateUnitPrice(@Param("storeProductId") StoreProductId storeProductId, @Param("newPrice") BigDecimal newPrice);
}
