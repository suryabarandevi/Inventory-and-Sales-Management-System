package com.Inventory.and.Sales.Management.System.IASMS.Services;


import com.Inventory.and.Sales.Management.System.IASMS.Model.Category;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategoriesByCompanyId(Long companyId) {
        return categoryRepository.findByCompanyId(companyId);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }


    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).get();
    }

}
