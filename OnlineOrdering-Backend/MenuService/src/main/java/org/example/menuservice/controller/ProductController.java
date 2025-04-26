package org.example.menuservice.controller;

import org.example.menuservice.dto.ProductDTO;
import org.example.menuservice.dto.StoreProductDTO;
import org.example.menuservice.entity.Product;
import org.example.menuservice.entity.StoreProduct;
import org.example.menuservice.entity.StoreProductId;
import org.example.menuservice.service.ProductService;
import org.example.menuservice.service.StoreProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/product-admin")
public class ProductController {
    private final ProductService productService;
    private final StoreProductService storeProductService;

    public ProductController(ProductService productService, StoreProductService storeProductService) {
        this.productService = productService;
        this.storeProductService = storeProductService;
    }

    // ----- products -----
    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        List<StoreProduct> storeProducts = storeProductService.findStoreProductsByProductId(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("product", product);
        response.put("availableStores", storeProducts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
        Product product = productService.createProductFromDTO(productDTO);
        product = productService.insertProduct(product);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/update-stock&price")
    public ResponseEntity<String> updateStockAndPrice(@RequestBody StoreProductDTO storeProductDTO) {
        StoreProductId storeProductId = new StoreProductId(storeProductDTO.getStoreId(), storeProductDTO.getProductId());
        if (storeProductService.existsById(storeProductId)) {
            storeProductService.updateStock(storeProductId, storeProductDTO.getStock());
            storeProductService.updateUnitPrice(storeProductId, storeProductDTO.getUnitPrice());
        } else {
            StoreProduct storeProduct = storeProductService.createStoreProductFromDTO(storeProductDTO);
            storeProductService.insertStoreProduct(storeProduct);

        }
        return ResponseEntity.ok("Stock and Price updated successfully!");
    }
}
