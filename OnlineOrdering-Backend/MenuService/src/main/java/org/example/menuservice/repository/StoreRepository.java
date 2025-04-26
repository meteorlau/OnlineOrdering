package org.example.menuservice.repository;

import org.example.menuservice.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // find all active stores
    List<Store> findByIsActiveTrue();

    // find all stores by name
    List<Store> findByName(String name);

    // find a active store
    Optional<Store> findByIsActiveTrueAndLocation(String location);

    // find by location
    Optional<Store> findByLocation(String location);

    // find by store name and location
    Optional<Store> findByNameAndLocation(String name, String location);

    // read existence
    boolean existsByLocation(String location);

    // update isActive
    @Modifying
    @Transactional
    @Query("UPDATE Store s SET s.isActive = false WHERE s.id = :storeId")
    int deactivateStoreById(@Param("storeId") Long storeId);

    // update isActive
    @Modifying
    @Transactional
    @Query("UPDATE Store s SET s.location = :storeLocation WHERE s.id = :storeId")
    int updateLocation(@Param("storeId") Long storeId, @Param("storeLocation") String storeLocation);

    // update isActive
    @Modifying
    @Transactional
    @Query("UPDATE Store s SET s.name = :storeName WHERE s.id = :storeId")
    int updateName(@Param("storeId") Long storeId, @Param("storeName")String storeName);
}
