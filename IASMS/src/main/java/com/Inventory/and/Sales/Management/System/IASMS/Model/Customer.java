package com.Inventory.and.Sales.Management.System.IASMS.Model;


import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String state;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
//    private List<OrderMapping> orderMappings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//
//    public List<OrderMapping> getOrderMappings() {
//        return orderMappings;
//    }
//
//    public void setOrderMappings(List<OrderMapping> orderMappings) {
//        this.orderMappings = orderMappings;
//    }
}