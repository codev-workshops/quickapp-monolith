package com.quickapp.order.dto;

import java.math.BigDecimal;

public class OrderDetailDTO {
    private int id;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal discount;
    private int productId;
    private String productName;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}
