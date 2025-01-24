package com.Inventory.and.Sales.Management.System.IASMS.Repository;


import com.Inventory.and.Sales.Management.System.IASMS.Model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId);

    List<Order> findByCompanyId(Long companyId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.company.id= :companyId")
    int findNoOfOrders(@Param("companyId") Long companyId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.orderId = :orderId")
    void delete(@Param("orderId") String orderId);

}
