package org.example.cartservice.dto;

public class CartItemResponse {
    private Long itemId;
    private String name;
    private int quantity;
    private double price;
    private String storeName;
    private String storeLocation;

    public CartItemResponse() {}

    public CartItemResponse(Long itemId, String name, int quantity, double price, String storeName, String storeLocation) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
    }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getStoreLocation() { return storeLocation; }
    public void setStoreLocation(String storeLocation) { this.storeLocation = storeLocation; }
}

