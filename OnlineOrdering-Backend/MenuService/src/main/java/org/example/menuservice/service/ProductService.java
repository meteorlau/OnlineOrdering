package org.example.menuservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.menuservice.dto.ProductDTO;
import org.example.menuservice.entity.Product;
import org.example.menuservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ----- Create -----
    public boolean productExists(String productName) {
        return productRepository.existsByName(productName);
    }

    @Transactional
    public Product insertProduct(Product product) {
        if (product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (productExists(product.getName())) {
            throw new IllegalArgumentException("Product already exists with this name.");
        }
        return productRepository.save(product);
    }

    // ----- Read -----
    public Product getProductByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productName));
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
    }

    public List<Product> findAllProductsWithKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    // ----- Update -----
    @Transactional
    public void updateDescription(String productName, String productDescription) {
        Product product = getProductByName(productName);

        if (product.getDescription().equals(productDescription)) {
            throw new IllegalArgumentException("Product description remains the same.");
        }
        int updated = productRepository.updateDescription(product.getId(), productDescription);
        if (updated == 0) {
            throw new IllegalStateException("Failed to update description.");
        }
    }

    // ----- DTO -----
    public Product createProductFromDTO(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImageUrl(productDTO.getImageUrl());
        return product;
    }
}
