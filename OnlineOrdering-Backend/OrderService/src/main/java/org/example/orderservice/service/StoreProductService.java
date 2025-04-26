package org.example.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.orderservice.dto.StoreProductDTO;
import org.example.orderservice.entity.*;
import org.example.orderservice.repository.StoreProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StoreProductService {
    private final StoreProductRepository storeProductRepository;
    private final StoreService storeService;
    private final ProductService productService;


    public StoreProductService(StoreProductRepository storeProductRepository, StoreService storeService, ProductService productService) {
        this.storeProductRepository = storeProductRepository;
        this.storeService = storeService;
        this.productService = productService;
    }

    // ----- Create -----
    public boolean existsById(StoreProductId storeProductId) {
        return storeProductRepository.existsById(storeProductId);
    }
    @Transactional
    public StoreProduct insertStoreProduct(StoreProduct storeProduct) {
        Store store = storeProduct.getStore();
        Product product = storeProduct.getProduct();
        BigDecimal unitPrice = storeProduct.getUnitPrice();
        if (store == null || product == null || unitPrice == null) {
            throw new IllegalArgumentException("Store and product and unit price must not be null.");
        }

        StoreProductId storeProductId = new StoreProductId(store.getId(), product.getId());
        if (existsById(storeProductId)) {
            throw new IllegalArgumentException("Store product already exists.");
        }
        if (storeProduct.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        storeProduct.setId(new StoreProductId(store.getId(), product.getId()));
        return storeProductRepository.save(storeProduct);
    }

    // ----- Read -----
    public StoreProduct getStoreProductById(StoreProductId storeProductId) {
        return storeProductRepository.findById(storeProductId)
                .orElseThrow(() -> new EntityNotFoundException("Store does not sell this product."));
    }

    public List<StoreProduct> findStoreProductsByStoreId(Long storeId) {
        return storeProductRepository.findByStore_Id(storeId);
    }

    public List<StoreProduct> findStoreProductsByProductId(Long productId) {
        return storeProductRepository.findByProduct_Id(productId);
    }

    // ----- Update -----
    @Transactional
    public void updateStock(StoreProductId storeProductId, int newStock) {
        StoreProduct storeProduct = getStoreProductById(storeProductId);

        if (newStock < 0) {
            throw new IllegalArgumentException("Stock number cannot be negative.");
        }

        int updated = storeProductRepository.updateStock(storeProduct.getId(), newStock);
        if (updated == 0) {
            throw new IllegalStateException("Failed to update stock number.");
        }
    }

    @Transactional
    public void updateUnitPrice(StoreProductId storeProductId, BigDecimal newPrice) {
        StoreProduct storeProduct = getStoreProductById(storeProductId);

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        int updated = storeProductRepository.updateUnitPrice(storeProduct.getId(), newPrice);
        if (updated == 0) {
            throw new IllegalStateException("Failed to update unit price.");
        }
    }

    // ----- DTO -----
    public StoreProduct createStoreProductFromDTO(StoreProductDTO storeProductDTO) {
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setStore(storeService.getActiveStoreById(storeProductDTO.getStoreId()));
        storeProduct.setProduct(productService.getProductById(storeProductDTO.getProductId()));
        storeProduct.setStock(storeProductDTO.getStock());
        storeProduct.setUnitPrice(storeProductDTO.getUnitPrice());
        return storeProduct;
    }
}
