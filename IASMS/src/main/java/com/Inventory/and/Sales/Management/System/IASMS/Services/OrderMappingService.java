package com.Inventory.and.Sales.Management.System.IASMS.Services;

import com.Inventory.and.Sales.Management.System.IASMS.Model.OrderMapping;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.OrderMappingRepository;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;


@Service
public class OrderMappingService {

    @Autowired
    private OrderMappingRepository orderMappingRepository;




    public OrderMapping updateOrderMapping(Long id,OrderMapping UpdatedorderMapping){
                 OrderMapping oldOrdermapping=orderMappingRepository.findById(id).get();
                 oldOrdermapping.setStatus("Paid");
                 oldOrdermapping.setDate(LocalDateTime.now());

                 return orderMappingRepository.save(oldOrdermapping);

    }





    public List<OrderMapping> getOrderMappingById(Long companyId) {
        return orderMappingRepository.findByCompanyId(companyId);
    }

    public OrderMapping getById(Long id) {
        return orderMappingRepository.findById(id).get();
    }

//    public void getSalesData(Map<Integer,Integer> h,Long companyId) {
//
//
//
//        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
//
//
//
//
//
//        List<OrderMapping> orderMappings = orderMappingRepository.findByStatusAndCompanyId("Paid",companyId);
//
//        for (OrderMapping o : orderMappings) {
//            LocalDateTime orderDate = o.getDate();
//
//
//
//            if (orderDate.isAfter(oneWeekAgo) || orderDate.toLocalDate().isEqual(oneWeekAgo.toLocalDate())) {
//
//                int dayOfWeek = orderDate.getDayOfWeek().getValue() - 1;
//
//
//                h.put(dayOfWeek, h.getOrDefault(dayOfWeek, 0) + 1);
//            }
//        }
//}
public void getSalesData(Map<Integer,Integer> h, Long companyId) {

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfWeek = now.with(ChronoField.DAY_OF_WEEK, 1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0);

    List<OrderMapping> orderMappings = orderMappingRepository.findByStatusAndCompanyId("Paid", companyId);

    for (OrderMapping o : orderMappings) {
        LocalDateTime orderDate = o.getDate();


        if (orderDate.isAfter(startOfWeek.minusSeconds(1)) &&
                orderDate.isBefore(startOfWeek.plusWeeks(1))) {

            int dayOfWeek = orderDate.getDayOfWeek().getValue() - 1;
            h.put(dayOfWeek, h.getOrDefault(dayOfWeek, 0) + 1);
        }
    }
}

    public Integer totalRevenue(Long comapanyid) {

        return orderMappingRepository.findTotalRevenue(comapanyid);
    }

    public void getSalesDataByYear(Map<Integer, Map<String, Integer>> yearlySalesData, Long companyId, int year) {
        List<OrderMapping> orderMappings = orderMappingRepository.findByStatusAndCompanyId("Paid", companyId);

        for (OrderMapping order : orderMappings) {
            LocalDateTime orderDate = order.getDate();

            if (orderDate.getYear() == year) {
                int month = orderDate.getMonthValue();

                Map<String, Integer> monthlyData = yearlySalesData.get(month);
                monthlyData.put("salesCount", monthlyData.get("salesCount") + 1);

                monthlyData.put("revenue", monthlyData.get("revenue") + order.getAmount().intValue());

                yearlySalesData.put(month, monthlyData);
            }
        }
    }

}
