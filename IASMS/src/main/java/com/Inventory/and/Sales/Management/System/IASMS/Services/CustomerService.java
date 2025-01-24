package com.Inventory.and.Sales.Management.System.IASMS.Services;


import com.Inventory.and.Sales.Management.System.IASMS.DTO.CustomerDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Customer;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }


    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }



    public CustomerDTO updateCustomer(Long id, Customer customerDetails) {

        Customer existingCustomer = getCustomerById(id);


        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setLastName(customerDetails.getLastName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setCompany(existingCustomer.getCompany());
        existingCustomer.setAddress(customerDetails.getAddress());
        existingCustomer.setCity(customerDetails.getCity());
        existingCustomer.setState(customerDetails.getState());
        customerRepository.save(existingCustomer);
        CustomerDTO customerDTO=new CustomerDTO(customerDetails.getId(),customerDetails.getFirstName(),customerDetails.getLastName(),customerDetails.getEmail(),customerDetails.getAddress(),customerDetails.getCity(),customerDetails.getState());


        return customerDTO;
    }


    public void deleteCustomer(Long id) {
//        Customer customer = getCustomerById(id);
//        customerRepository.deleteById(id);
        Customer customer = customerRepository.findById(id).get();


        try {
            customerRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Cannot delete customer with id " + id
                    + " because it is associated with existing order mappings");
        }
    }

    public List<Customer> getCustomersByCompanyId(Long companyId) {
        return customerRepository.findByCompanyId(companyId);
    }


    public boolean isUserExists(String email) {
        if(customerRepository.findUserByEmail(email)){
            return true;
        }
        return false;
    }
}
