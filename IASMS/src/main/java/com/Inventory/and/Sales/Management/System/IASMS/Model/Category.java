package com.Inventory.and.Sales.Management.System.IASMS.Model;



import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
//    private Long adminId;

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

//    public Manager getManager() {
//        return manager;
//    }
//
//    public void setManager(Manager manager) {
//        this.manager = manager;
//    }

//    public List<Product> getProducts() {
//        return products;
//    }
//
//    public void setProducts(List<Product> products) {
//        this.products = products;
//    }

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

//    @ManyToOne
//    @JoinColumn(name = "manager_id")
//    private Manager manager;
//
//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
//    private List<Product> products;
}