package org.example.menuservice.controller;

import org.example.menuservice.dto.StoreDTO;
import org.example.menuservice.entity.Store;
import org.example.menuservice.entity.StoreProduct;
import org.example.menuservice.service.StoreProductService;
import org.example.menuservice.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/store-admin")
public class StoreController {
    private final StoreProductService storeProductService;
    private final StoreService storeService;

    public StoreController(StoreProductService storeProductService, StoreService storeService) {
        this.storeProductService = storeProductService;
        this.storeService = storeService;
    }

    // ----- store management -----
    @GetMapping("/stores")
    public ResponseEntity<List<Store>> findAllStores() {
        return ResponseEntity.ok(storeService.findAllStores());
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getStore(@PathVariable Long storeId) {
        Store store = storeService.getStoreById(storeId);
        List<StoreProduct> storeProducts = storeProductService.findStoreProductsByStoreId(storeId);

        Map<String, Object> response = new HashMap<>();
        response.put("store", store);
        response.put("availableProducts", storeProducts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-store")
    public ResponseEntity<Store> addStore(@RequestBody StoreDTO storeDTO) {
        Store store = storeService.createStoreFromDTO(storeDTO);
        store = storeService.insertStore(store);
        return ResponseEntity.ok(store);
    }

    @PostMapping("/stores/{storeId}/deactivate")
    public ResponseEntity<String> deactivateStore(@PathVariable Long storeId) {
        storeService.deactivateStore(storeId);
        return ResponseEntity.ok("Store deactivated successfully!");
    }
}
