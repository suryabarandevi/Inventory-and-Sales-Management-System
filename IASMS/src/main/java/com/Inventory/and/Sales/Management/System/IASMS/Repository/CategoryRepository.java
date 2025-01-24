package com.Inventory.and.Sales.Management.System.IASMS.Repository;


import com.Inventory.and.Sales.Management.System.IASMS.Model.Category;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findCategoriesByCompanyId(Long companyId);
    List<Category> findByCompanyId(Long Id);
}

