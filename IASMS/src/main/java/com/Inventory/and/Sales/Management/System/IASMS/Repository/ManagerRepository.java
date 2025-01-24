package com.Inventory.and.Sales.Management.System.IASMS.Repository;


import com.Inventory.and.Sales.Management.System.IASMS.DTO.ManagerDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {



     List<Manager> findByCompanyId(Long Id);

    boolean existsByEmail(String email);



    @Query("SELECT m.company.id FROM Manager m WHERE m.email = :email")
    Long findCompanyIdByEmail(@Param("email") String email);
}
