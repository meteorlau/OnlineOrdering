package org.example.menuservice.controller;

import org.example.menuservice.dto.MenuItem;
import org.example.menuservice.dto.ProductWithStoresDTO;
import org.example.menuservice.entity.Product;
import org.example.menuservice.entity.StoreProduct;
import org.example.menuservice.service.ProductService;
import org.example.menuservice.service.StoreProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final ProductService productService;
    private final StoreProductService storeProductService;

    public MenuController(ProductService productService, StoreProductService storeProductService) {
        this.productService = productService;
        this.storeProductService = storeProductService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductWithStoresDTO>> getAllProductWithStores() {
        List<Product> products = productService.findAllProducts();
        List<ProductWithStoresDTO> result = new ArrayList<>();

        for (Product p : products) {
            List<StoreProduct> stores = storeProductService.findStoreProductsByProductId(p.getId());
            result.add(new ProductWithStoresDTO(p, stores));
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        List<StoreProduct> stores = storeProductService.findStoreProductsByProductId(id);

        if (stores.isEmpty()) {
            return ResponseEntity.badRequest().body("no stores selling this product.");
        }

        StoreProduct selectedStore = stores.get(0); // 只取第一個供應商

        MenuItem dto = new MenuItem(
                product.getId(),
                product.getName(),
                selectedStore.getStock(),
                selectedStore.getUnitPrice(),
                selectedStore.getStore().getName(),
                selectedStore.getStore().getLocation()
        );

        return ResponseEntity.ok(dto);
    }
}

