package com.Inventory.and.Sales.Management.System.IASMS.Services;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.ProductDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Order;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Product;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.OrderMappingRepository;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;


    @Autowired
    private OrderMappingRepository orderMappingRepository;

    @Autowired
    private ProductService productService;







    public Order saveOrder(Order order) {

        return ordersRepository.save(order);
    }


    public void deleteOrder(Order order, List<ProductDTO> productDTOS) {

        for(ProductDTO productDTO:productDTOS){
            Product product=productService.getProductbyId(productDTO.getId());
            product.setQuantity(product.getQuantity()+productDTO.getQuantity());
            productService.updateProduct(productDTO.getId(),product);
        }
        if (order!=null) {

            ordersRepository.delete(order);
        }

//        ordersRepository.delete(id);
    }



    public Order getOrderById(String orderId) {
        return ordersRepository.findByOrderId(orderId);
    }

    public List<Order> getOrdersByCompanyId(Long companyId) {
        return ordersRepository.findByCompanyId(companyId);
    }

    public int getNoOfOrder(Long companyId) {
        return ordersRepository.findNoOfOrders(companyId);
    }


}
