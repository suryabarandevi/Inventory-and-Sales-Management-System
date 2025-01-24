package com.Inventory.and.Sales.Management.System.IASMS.Repository;


import com.Inventory.and.Sales.Management.System.IASMS.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCompanyId(Long id);

    @Query("SELECT p FROM Product p WHERE p.Id = :Id")
    Product findByProductId(long Id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.company.id = :companyId")
    int findNoOfProductsByCompanyId(@Param("companyId")Long companyId);

    @Query("SELECT p FROM Product p WHERE p.company.id = :companyId AND p.quantity < 20")
    List<Product> findLowStockProducts(Long companyId);
}
