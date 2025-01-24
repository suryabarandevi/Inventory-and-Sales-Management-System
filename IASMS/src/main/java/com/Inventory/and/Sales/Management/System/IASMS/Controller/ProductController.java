package com.Inventory.and.Sales.Management.System.IASMS.Controller;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.ManagerDTO;
import com.Inventory.and.Sales.Management.System.IASMS.DTO.ProductDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Category;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Product;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CategoryService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CompanyService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ManagerService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {


    @Autowired
    private ProductService productService;


    @Autowired
    private CompanyService companyService;


    @Autowired
    private ManagerService managerService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductDTO>> getProductsByCompanyId(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long companyId=0L;

        if(companyService.isUserExists(email)){
            companyId = companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId =managerService.getCompanyId(email);
            System.out.println(companyId);
        }
//        Company company = companyService.getCompanyById(companyId).getBody();


        List<ProductDTO> productDTOS=new ArrayList<>();

        List<Product> products = productService.getProductsByCompanyId(companyId);
        if(products.size()==0)return ResponseEntity.ok(productDTOS);
        for(Product product:products){
            ProductDTO productDTO=new ProductDTO(product.getId(),product.getName(),product.getCategory().getId(),product.getQuantity(),product.getPrice());
            productDTOS.add(productDTO);
        }

        if (productDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(productDTOS);

    }

    @PostMapping(value ="/create/{category_id}" )
    public ResponseEntity<Product>createProduct(@RequestBody Product product,
                                 @PathVariable Long category_id, @AuthenticationPrincipal OAuth2User principal) throws IOException {
        String email = principal.getAttribute("email");
        Long companyId=0L;
        if(companyService.isUserExists(email)){
            companyId=companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId= managerService.getCompanyId(email);
        }

        Company company = companyService.getCompanyById(companyId).getBody();
        product.setCompany(company);
        product.setCategory(categoryService.getCategoryById(category_id));
//        System.out.println(product.toString());
        productService.createProduct(product);
        return ResponseEntity.ok(product);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/update/{id}/{category_id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@PathVariable Long category_id, @RequestBody Product productDetails) {
        System.out.println(id);


        return ResponseEntity.ok(productService.updateProductnew(id,category_id, productDetails));
    }

    @GetMapping("/noOfProducts")
    public ResponseEntity<Map<String,String>> noOfProducts(@AuthenticationPrincipal OAuth2User principal){
        String email=principal.getAttribute("email");
        Map<String,String> h=new HashMap<>();
        Long companyId=0L;
        if(companyService.isUserExists(email)){
            companyId=companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId=managerService.getCompanyId(email);
        }
        int noOfProducts=productService.getNoOfProductsByCompanyId(companyId);

        h.put("Products",String.valueOf(noOfProducts));
        return ResponseEntity.ok(h);
    }
    @GetMapping("/lowStockProducts")
    public ResponseEntity<List<ProductDTO>> lowOfProducts(@AuthenticationPrincipal OAuth2User principal){
        String email = principal.getAttribute("email");
        Long companyId=0L;

        if(companyService.isUserExists(email)){
            Company company = companyService.getCompanyByEmail(email).getBody();

            companyId=company.getId();
        } else if (managerService.isUserExists(email)) {
            companyId = managerService.getCompanyId(email);


        }
        List<Product> products = productService.getLowStockProducts(companyId);
        List<ProductDTO> productDTOS=new ArrayList<>();
        if(products.size()==0)return ResponseEntity.ok(productDTOS);
        for (Product product:products){
            productDTOS.add(new ProductDTO(product.getId(),product.getName(),product.getCategory().getId(),product.getQuantity(),product.getPrice()));
        }
        System.out.println(products.toString());


        return ResponseEntity.ok(productDTOS);

    }
}

