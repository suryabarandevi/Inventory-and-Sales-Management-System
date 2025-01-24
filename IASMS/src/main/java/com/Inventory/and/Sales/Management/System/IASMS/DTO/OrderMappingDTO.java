package com.Inventory.and.Sales.Management.System.IASMS.DTO;

import java.time.LocalDateTime;

public class OrderMappingDTO {
    private Long id;
    private String orderId;
    private Long customerId;
    private String status;
    private Double productPrice;
    private LocalDateTime date;


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderMappingDTO(Long id, String orderId, Long customerId, String status,Double productPrice,LocalDateTime date) {
        this.id=id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.productPrice=productPrice;
        this.date=date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
