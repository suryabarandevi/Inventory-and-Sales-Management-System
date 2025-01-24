package com.Inventory.and.Sales.Management.System.IASMS.Repository;

import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {


    Company findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT c.id FROM Company c WHERE c.email = :email")
    Long getCompanyId(@Param("email")String email);


}

