package com.Inventory.and.Sales.Management.System.IASMS.Model;


import jakarta.persistence.*;


@Entity

@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;
    private Double price;

//    public String getSku() {
//        return sku;
//    }
//
//    public void setSku(String sku) {
//        this.sku = sku;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

//    public Long getAdminId() {
//        return adminId;
//    }
//
//    public void setAdminId(Long adminId) {
//        this.adminId = adminId;
//    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }



//    private String sku;
//    private Long adminId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;

//    @ManyToOne
//    @JoinColumn(name = "manager_id")
//    private Manager manager;
}