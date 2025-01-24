package com.Inventory.and.Sales.Management.System.IASMS.DTO;

public class CategoryDTO {
    private Long id;

    public String getName() {
        return name;
    }

    public CategoryDTO(Long id,String name) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;
}
