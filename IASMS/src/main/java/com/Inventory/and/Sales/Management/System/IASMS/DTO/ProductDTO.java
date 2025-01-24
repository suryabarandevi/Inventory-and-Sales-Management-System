package com.Inventory.and.Sales.Management.System.IASMS.DTO;

public class ProductDTO
{

    private Long id;
    private String name;
    private Long category_Id;
    //    private Long company_id;
    private Integer quantity;
    private Double price;

    public ProductDTO(Long id, String name, Long category_Id, Integer quantity, Double price) {
        this.id = id;
        this.name = name;
        this.category_Id = category_Id;
        this.quantity = quantity;
        this.price = price;
    }

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

    public Long getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(Long category_Id) {
        this.category_Id = category_Id;
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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category_Id=" + category_Id +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }


}
