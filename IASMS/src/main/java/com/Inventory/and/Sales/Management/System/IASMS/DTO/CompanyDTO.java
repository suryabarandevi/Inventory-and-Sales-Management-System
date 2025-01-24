package com.Inventory.and.Sales.Management.System.IASMS.DTO;

import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;

public class CompanyDTO {
    private String name;
    private String email;
    private String address;
    private String phone;

    public CompanyDTO(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
