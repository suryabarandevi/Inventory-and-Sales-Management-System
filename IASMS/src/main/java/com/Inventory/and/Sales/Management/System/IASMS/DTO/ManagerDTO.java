package com.Inventory.and.Sales.Management.System.IASMS.DTO;

public class ManagerDTO {
    private  Long id;
    private String name;
    private String email;

    public Long getId() {
        return id;
    }

    public ManagerDTO(Long id, String name, String email, String phone) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String phone;

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


}
