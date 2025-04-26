package org.example.cartservice.entity;

import java.math.BigDecimal;

public class MenuItem {
    private Long id;
    private String name;
    private int stock;
    private double price;
    private String storeName;
    private String storeLocation;

    public MenuItem() {}

    public MenuItem(Long id, String name,int stock, double price, String storeName, String storeLocation) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getStoreLocation() { return storeLocation; }
    public void setStoreLocation(String storeLocation) { this.storeLocation = storeLocation; }
}

