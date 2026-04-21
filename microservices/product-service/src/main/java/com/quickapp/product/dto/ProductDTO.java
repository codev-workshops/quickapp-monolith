package com.quickapp.product.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private String icon;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;
    private int unitsInStock;
    private boolean isActive;
    private boolean isDiscontinued;
    private int productCategoryId;
    private String productCategoryName;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public BigDecimal getBuyingPrice() { return buyingPrice; }
    public void setBuyingPrice(BigDecimal buyingPrice) { this.buyingPrice = buyingPrice; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }
    public int getUnitsInStock() { return unitsInStock; }
    public void setUnitsInStock(int unitsInStock) { this.unitsInStock = unitsInStock; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public boolean getIsDiscontinued() { return isDiscontinued; }
    public void setIsDiscontinued(boolean isDiscontinued) { this.isDiscontinued = isDiscontinued; }
    public int getProductCategoryId() { return productCategoryId; }
    public void setProductCategoryId(int productCategoryId) { this.productCategoryId = productCategoryId; }
    public String getProductCategoryName() { return productCategoryName; }
    public void setProductCategoryName(String productCategoryName) { this.productCategoryName = productCategoryName; }
}
