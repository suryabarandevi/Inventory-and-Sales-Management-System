package com.Inventory.and.Sales.Management.System.IASMS.Model;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.ProductDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id", updatable = false, nullable = false)
    private String orderId; // Primary Key

    @Column(name = "products", columnDefinition = "TEXT")
    private String products;


    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

//    public List<ProductDTO> getProduct() {
//        return product;
//    }
//    public void setProduct(List<ProductDTO> product) {
//        this.product = product;
//    }



//    private List<ProductDTO> product;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }



    private Double productPrice;
    private String status;
    private Integer quantity;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMapping> orderMappings;



}
