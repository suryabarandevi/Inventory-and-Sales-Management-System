package com.Inventory.and.Sales.Management.System.IASMS.Model;


import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "managers")
public class Manager {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
//
//    @OneToMany(mappedBy = "manager")
//    private List<Category> categories;
//
//    @OneToMany(mappedBy = "manager")
//    private List<Product> products;
}