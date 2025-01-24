package com.Inventory.and.Sales.Management.System.IASMS.Repository;

import com.Inventory.and.Sales.Management.System.IASMS.Model.OrderMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderMappingRepository extends JpaRepository<OrderMapping, Long> {

    List<OrderMapping> findByCompanyId(Long companyId);

    @Query("SELECT om FROM OrderMapping om WHERE om.status = :status AND om.company.id = :companyId")
    List<OrderMapping> findByStatusAndCompanyId(@Param("status") String status, @Param("companyId") Long companyId);

    @Query("SELECT SUM(amount) FROM OrderMapping om WHERE om.company.id = :companyId AND om.status='Paid'")
    Integer findTotalRevenue(@Param("companyId")Long comapanyId);
}