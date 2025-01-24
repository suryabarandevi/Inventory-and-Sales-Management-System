package com.Inventory.and.Sales.Management.System.IASMS.Repository;

import com.Inventory.and.Sales.Management.System.IASMS.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCompanyId(Long companyId);


    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    boolean findUserByEmail(@Param("email")String email);
}
