package org.example.menuservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.menuservice.dto.StoreDTO;
import org.example.menuservice.entity.Store;
import org.example.menuservice.repository.StoreRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;


    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    // ----- Create -----
    public boolean activeStoreExists(String storeLocation) {
        return storeRepository.findByIsActiveTrueAndLocation(storeLocation).isPresent();
    }

    @Transactional
    public Store insertStore(Store store) {
        if (store.getName().isEmpty() || store.getLocation().isEmpty()) {
            throw new IllegalArgumentException("Store name and location must not be null.");
        }
        if (activeStoreExists(store.getLocation())) {
            throw new IllegalArgumentException("Store already exists with this location.");
        }
        return storeRepository.save(store);
    }

    // ----- Read -----
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + storeId));
    }

    public Store getActiveStoreByNameAndLocation(String name, String location) {
        Store store = storeRepository.findByNameAndLocation(name, location)
                .orElseThrow(() -> new EntityNotFoundException("Store not found."));
        if (Boolean.FALSE.equals(store.getIsActive())) {
            throw new DisabledException("Store is not active.");
        }
        return store;
    }

    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public List<Store> findActiveStores() {
        return storeRepository.findByIsActiveTrue();
    }

    public List<Store> findStoresByName(String name) {
        return storeRepository.findByName(name);
    }

    // ----- Update -----
    @Transactional
    public Store updateStore(Long storeId, Store updatedStore) {
        Store store = getActiveStoreById(storeId);

        if (updatedStore.getName() == null || updatedStore.getLocation() == null) {
            throw new IllegalArgumentException("Store name and location must not be null.");
        }
        if (activeStoreExists(updatedStore.getLocation())) {
            throw new IllegalArgumentException("Store already exists at this location.");
        }

        store.setName(updatedStore.getName());
        store.setLocation(updatedStore.getLocation());
        return storeRepository.save(store);
    }



    public Store getActiveStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id."));
        if (Boolean.FALSE.equals(store.getIsActive())) {
            throw new DisabledException("Store is not active.");
        }
        return store;
    }

    @Transactional
    public void deactivateStore(Long storeId) {
        Store store = getActiveStoreById(storeId);

        int updated = storeRepository.deactivateStoreById(storeId);
        if (updated == 0) {
             throw new IllegalArgumentException("Failed to deactivate store.");
        }
    }

    // ----- DTO -----
    public Store createStoreFromDTO(StoreDTO storeDTO) {
        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setLocation(storeDTO.getLocation());
        return store;
    }
}
