package com.Inventory.and.Sales.Management.System.IASMS.Controller;


import com.Inventory.and.Sales.Management.System.IASMS.DTO.OrderMappingDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Customer;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Order;
import com.Inventory.and.Sales.Management.System.IASMS.Model.OrderMapping;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.CustomerRepository;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.OrderMappingRepository;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.OrdersRepository;
import com.Inventory.and.Sales.Management.System.IASMS.Services.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/orders")

@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*",allowCredentials = "true")
public class OrderMappingController {

    @Autowired
    private OrderMappingService orderMappingService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ManagerService managerService;



    @Autowired
    private OrderMappingRepository orderMappingRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @PostMapping("/ordermapping")
    public ResponseEntity<OrderMappingDTO> createOrderMapping(@RequestBody OrderMappingDTO request) {


        Order order = ordersRepository.findByOrderId(request.getOrderId());
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        System.out.println("ordermapping");
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.setOrder(order);
        orderMapping.setCustomer(customer);
        orderMapping.setStatus("Pending");
        orderMapping.setAmount(order.getProductPrice());
        orderMapping.setDate(LocalDateTime.now());
        System.out.println(LocalDateTime.now());


        Company company = order.getCompany();
        if (company != null) {
            orderMapping.setCompany(company);
        }


        orderMappingRepository.save(orderMapping);

        try {
            emailService.sendApprovalEmail(customer.getFirstName(), customer.getEmail(),
                    orderMapping.getAmount().toString(), orderMapping.getOrder().getOrderId(), company.getName());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(request);
    }

    @PutMapping("/ordermapping/sendInvoice/{id}/{email}/{orderid}")
    public ResponseEntity<Boolean> sendInvoice(@PathVariable Long id,@PathVariable String email,@PathVariable String orderid){

        OrderMapping orderMapping=orderMappingService.getById(id);


        Order order=ordersService.getOrderById(orderMapping.getOrder().getOrderId());
        Company company= companyService.getCompanyById(orderMapping.getCompany().getId()).getBody();

            order.setStatus("Delivered");
            ordersService.saveOrder(order);



        orderMapping.setStatus("Paid");
        Customer customers=customerService.getCustomerById(orderMapping.getCustomer().getId());
        emailService.sendPaymentEmail(customers.getFirstName(), email,
                orderMapping.getAmount().toString(), order.getOrderId(),company.getName(),order);

        OrderMapping savedOrder = orderMappingService.updateOrderMapping(orderMapping.getId(),orderMapping);



        return ResponseEntity.ok(true);
    }


    @GetMapping("/ordermapping/get")
    public ResponseEntity<List<OrderMappingDTO>> getAllOrderMappings(@AuthenticationPrincipal OAuth2User principal) {
        String email=principal.getAttribute("email");
        Long companyId=0L;
        if(companyService.isUserExists(email)){
            companyId=companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId= managerService.getCompanyId(email);
        }

        List<OrderMappingDTO> orderMappingDTOS=new ArrayList<>();

        List<OrderMapping> orderMappings=orderMappingService.getOrderMappingById(companyId);
        if(orderMappings.size()==0) {
            return ResponseEntity.ok(orderMappingDTOS);
        }
        for(OrderMapping orderMapping:orderMappings){

            OrderMappingDTO orderMappingDTO=new OrderMappingDTO(orderMapping.getId(),orderMapping.getOrder().getOrderId(), orderMapping.getCustomer().getId(),orderMapping.getStatus(),orderMapping.getAmount(),orderMapping.getDate());

            orderMappingDTOS.add(orderMappingDTO);
        }


        return new ResponseEntity<>(orderMappingDTOS, HttpStatus.OK);
    }


    @GetMapping("/sales/data")
    public ResponseEntity<Map<Integer, Integer>> getSales(@AuthenticationPrincipal OAuth2User principal){
        String email=principal.getAttribute("email");
        Long cId=0L;
        if(companyService.isUserExists(email)){
            cId=companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            cId=managerService.getCompanyId(email);
        }
        Map<Integer,Integer> h=new HashMap<>();

        for(int i=0;i<7;i++){
            h.put(i,0);
        }

        orderMappingService.getSalesData(h,cId);


        return ResponseEntity.ok(h);
    }

    @GetMapping("/getRevenue")
    public ResponseEntity<String> getRevenue(@AuthenticationPrincipal OAuth2User principal){
        try {

            if (principal == null || !principal.getAttributes().containsKey("email")) {
                return new ResponseEntity<>("User is not authenticated or email is missing.", HttpStatus.UNAUTHORIZED);
            }

            String email = principal.getAttribute("email");
            Long id = 0L;


            if (managerService.isUserExists(email)) {
                id = managerService.getCompanyId(email);
            } else if (companyService.isUserExists(email)) {
                id = companyService.getCompanyId(email);
            } else {
                return new ResponseEntity<>("User does not exist in either the manager or company services.", HttpStatus.NOT_FOUND);
            }


            try {
                String revenue = orderMappingService.totalRevenue(id).toString();
                return ResponseEntity.ok(revenue);
            } catch (Exception e) {

                return new ResponseEntity<>("Error retrieving revenue: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {

            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/sales/data/{year}")
    public ResponseEntity<Map<Integer, Map<String, Integer>>> getSalesDataByYear(
            @PathVariable int year,
            @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long companyId = null;

        if (companyService.isUserExists(email)) {
            companyId = companyService.getCompanyId(email);
        } else if (managerService.isUserExists(email)) {
            companyId = managerService.getCompanyId(email);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


        Map<Integer, Map<String, Integer>> yearlySalesData = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Integer> monthlyData = new HashMap<>();
            monthlyData.put("salesCount", 0);
            monthlyData.put("revenue", 0);
            yearlySalesData.put(i, monthlyData);
        }

        orderMappingService.getSalesDataByYear(yearlySalesData, companyId, year);

        return ResponseEntity.ok(yearlySalesData);
    }

}



