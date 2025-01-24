package com.Inventory.and.Sales.Management.System.IASMS.Controller;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.CategoryDTO;
import com.Inventory.and.Sales.Management.System.IASMS.DTO.ManagerDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Category;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;

import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CategoryService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CompanyService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CompanyService companyService;


    @Autowired
    private ManagerService managerService;

    @GetMapping("/getCategories")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByCompanyId(@AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        Long companyId=0L;
        if(companyService.isUserExists(email)){
            companyId = companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId =managerService.getCompanyId(email);
        }
        List<CategoryDTO> categoriesDTOS=new ArrayList<>();

        List<Category> categories = categoryService.getCategoriesByCompanyId(companyId);
        for(Category category:categories){
            CategoryDTO categoryDTO=new CategoryDTO(category.getId(),category.getName());
            categoriesDTOS.add(categoryDTO);
        }

        if (categoriesDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(categoriesDTOS);

    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO>createCategory(@RequestBody CategoryDTO category, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long companyId=0L;

        if(companyService.isUserExists(email)){
            companyId = companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId =managerService.getCompanyId(email);
        }
        List<Category> categories=categoryService.getCategoriesByCompanyId(companyId);
        for(Category category1:categories){
            if(category1.getName().equals(category.getName())){
                return ResponseEntity.badRequest().build();
            }
        }
        Company company = companyService.getCompanyById(companyId).getBody();
        Category category1=new Category();
        category1.setName(category.getName());
        category1.setCompany(company);
        categoryService.createCategory(category1);


        return ResponseEntity.ok(new CategoryDTO(category1.getId(), category.getName()));
    }

}

