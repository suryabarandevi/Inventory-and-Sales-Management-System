package com.Inventory.and.Sales.Management.System.IASMS.Controller;


import com.Inventory.and.Sales.Management.System.IASMS.DTO.CustomerDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Customer;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CompanyService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CustomerService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ManagerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200",allowCredentials = "true",allowedHeaders = "*")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ManagerService managerService;

    @PostMapping("/create")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody Customer customer,@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long companyId=0L;

        if(companyService.isUserExists(email)){
            companyId=companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId= managerService.getCompanyId(email);
        }
        List<Customer> customers=customerService.getCustomersByCompanyId(companyId);

       for(Customer customer1:customers){
           if(customer1.getEmail().equals(customer.getEmail())){
               return  ResponseEntity.badRequest().build();
           }
       }
//        if(customerService.isUserExists(customer.getEmail())){
//
//            return ResponseEntity.badRequest().build();
//        }
        Company company = companyService.getCompanyById(companyId).getBody();
        customer.setCompany(company);
        Customer createdCustomer = customerService.createCustomer(customer);
        CustomerDTO customerDTO=new CustomerDTO(createdCustomer.getId(),createdCustomer.getFirstName(),createdCustomer.getLastName(),createdCustomer.getEmail(),createdCustomer.getAddress(),createdCustomer.getCity(),createdCustomer.getState());
        return ResponseEntity.ok(customerDTO);
    }


    @GetMapping("/get")
    public ResponseEntity<List<CustomerDTO>> getCustomerById(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long id=0L;
        if(companyService.isUserExists(email)) {
//            Company company = companyService.getCompanyByEmail(email).getBody();
            id = companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            id=managerService.getCompanyId(email);
        }
        List<CustomerDTO> customerDTOS=new ArrayList<>();
        List<Customer> customers = customerService.getCustomersByCompanyId(id);
        if(customers.size()==0)return ResponseEntity.ok(customerDTOS);
        for(Customer customer:customers){
            CustomerDTO customerDTO=new CustomerDTO(customer.getId(),customer.getFirstName(),customer.getLastName(),customer.getEmail(),customer.getAddress(),customer.getCity(),customer.getState());
            customerDTOS.add(customerDTO);
        }

        return ResponseEntity.ok(customerDTOS);
    }




    @PutMapping("update/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customerDetails) {

        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDetails);



        return ResponseEntity.ok(updatedCustomer);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete old customer: " + e.getMessage());
        }
    }
}
