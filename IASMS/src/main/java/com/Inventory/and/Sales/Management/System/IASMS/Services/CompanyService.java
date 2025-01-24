package com.Inventory.and.Sales.Management.System.IASMS.Services;



import com.Inventory.and.Sales.Management.System.IASMS.DTO.CompanyDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;


    public ResponseEntity<Company> saveCompany(Company company) {

        Company savedCompany = companyRepository.save(company);


        return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
    }


    public ResponseEntity<Company> getCompanyByEmail(String email) {

        Company companyExists=companyRepository.findByEmail(email);

        return new ResponseEntity<>(companyExists,HttpStatus.FOUND);
    }


    public Iterable<Company> getAllCompanies() {
        return companyRepository.findAll();
    }


    public ResponseEntity<Company> getCompanyById(Long id) {
        Company company = companyRepository.findById(id).orElse(null);

        if (company != null) {
            return ResponseEntity.ok(company);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    public Company findByEmail(String email) {

        return companyRepository.findByEmail(email);
    }

    public boolean updateCompany(Long id, CompanyDTO companyDetails) {
        if (!companyRepository.existsById(id)) {
            throw new IllegalArgumentException("Company not found");
        }

        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            throw new IllegalStateException("Unexpected null result from repository");
        }


        company.setName(companyDetails.getName());
        company.setEmail(companyDetails.getEmail());
        company.setAddress(companyDetails.getAddress());
        company.setPhone(companyDetails.getPhone());


        companyRepository.save(company);
        return true;
    }


    public boolean isUserExists(String email) {
        return companyRepository.existsByEmail(email);
    }

    public Long getCompanyId(String email) {
        return  companyRepository.getCompanyId(email);
    }




//    public List<Manager> getManagers(Long id) {
//        return  companyRepository.findManagers(id);
//    }
}
