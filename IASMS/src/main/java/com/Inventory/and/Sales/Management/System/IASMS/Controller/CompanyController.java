package com.Inventory.and.Sales.Management.System.IASMS.Controller;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.CompanyDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200",allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class CompanyController {

    @Autowired
    private CompanyService companyService;


    @PostMapping("/company")
    public ResponseEntity<Company> createOrUpdateCompany(@RequestBody Company company) {
        Company savedCompany = companyService.saveCompany(company).getBody();
        System.out.println(company);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @GetMapping("/email")
    public ResponseEntity<Map<String, CompanyDTO>> getCompanyByEmail( @AuthenticationPrincipal OAuth2User principal) {


        System.out.println("reachaed");
        String e=principal.getAttribute("email");
        Company company = companyService.findByEmail(e);
        CompanyDTO companyDTO=new CompanyDTO(company.getName(), company.getEmail(),company.getAddress(),company.getPhone());
        System.out.println(company.getEmail());
        System.out.println(company.getName());
        if (company != null) {

            Map<String, CompanyDTO> response = new HashMap<>();
            response.put("company", companyDTO);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/update")
    public boolean updateCompany(@RequestBody CompanyDTO company,@AuthenticationPrincipal OAuth2User principal) {
        try {
            Long id=companyService.getCompanyId(principal.getAttribute("email"));

            boolean isUpdated = companyService.updateCompany(id, company);
            if (isUpdated) {
                System.out.println("Company updated successfully");
                return true;
            } else {
                System.out.println("Company update failed");
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Company not found: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
            return false;
        }
    }




}
