package com.Inventory.and.Sales.Management.System.IASMS.Services;

import com.Inventory.and.Sales.Management.System.IASMS.Model.Product;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public Product createProduct(Product product) {

        return productRepository.save(product);
    }

    public boolean deleteProduct(Long id) {


        productRepository.deleteById(id);
        return true;
    }


    public Product updateProduct(Long id,Product productDetails) {

        return productRepository.save(productDetails);

    }

    public Product updateProductnew(Long id,Long categoryId, Product productDetails) {
        Product newProduct=productRepository.findByProductId(id);
        newProduct.setQuantity(productDetails.getQuantity());
        newProduct.setName(productDetails.getName());
        newProduct.setCategory(categoryService.getCategoryById(categoryId));
        newProduct.setPrice(productDetails.getPrice());

        return productRepository.save(newProduct);

    }

    public Product getProductbyId(long Id) {

        return productRepository.findByProductId(Id);
    }

    public int getNoOfProductsByCompanyId(Long companyId) {
        return productRepository.findNoOfProductsByCompanyId(companyId);
    }

    public List<Product> getLowStockProducts(Long companyId) {
        return productRepository.findLowStockProducts(companyId);
    }

    public List<Product> getProductsByCompanyId(Long id) {
        return productRepository.findByCompanyId(id);
    }
}
