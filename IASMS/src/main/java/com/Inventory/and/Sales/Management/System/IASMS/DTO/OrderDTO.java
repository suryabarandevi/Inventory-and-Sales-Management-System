package com.Inventory.and.Sales.Management.System.IASMS.DTO;

import com.Inventory.and.Sales.Management.System.IASMS.Model.Product;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    //    private Long productId;
    private Double productPrice;
    private String status;
    private Integer quantity;
    private LocalDateTime date;
    private List<ProductDTO> product;

    public OrderDTO(String orderId, Double productPrice, String status, Integer quantity, LocalDateTime date, List<ProductDTO> product) {
        this.orderId=orderId;
        this.productPrice = productPrice;
        this.status = status;
        this.quantity = quantity;
        this.date = date;
        this.product = product;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ProductDTO> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDTO> product) {
        this.product = product;
    }
}
