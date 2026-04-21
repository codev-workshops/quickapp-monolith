package com.quickapp.order.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private int id;
    private BigDecimal discount;
    private String comments;
    private String cashierId;
    private String cashierName;
    private int customerId;
    private String customerName;
    private List<OrderDetailDTO> orderDetails;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public String getCashierId() { return cashierId; }
    public void setCashierId(String cashierId) { this.cashierId = cashierId; }
    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public List<OrderDetailDTO> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetailDTO> orderDetails) { this.orderDetails = orderDetails; }
}
