package com.Inventory.and.Sales.Management.System.IASMS.Services;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.CompanyRepository;
import com.Inventory.and.Sales.Management.System.IASMS.Repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private CompanyRepository companyRepository;


    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }
    public boolean isUserExists(String email) {
        return managerRepository.existsByEmail(email);
    }


    public Manager getManagerById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with ID: " + id));
    }



    public Manager createManager(Manager manager, String userEmail) {

       Long companyId= companyRepository.getCompanyId(userEmail);


        manager.setCompany(companyRepository.findById(companyId).get());


        return managerRepository.save(manager);
    }


    public Manager updateManager(Long id, Manager updatedDetails) {
        Manager existingManager = getManagerById(id);
        existingManager.setName(updatedDetails.getName());
        existingManager.setPhone(updatedDetails.getPhone());
        return managerRepository.save(existingManager);
    }

    public boolean deleteManager(Long id) {
       Manager manager= managerRepository.findById(id).get();
        if(manager!=null){
//            Manager manager = getManagerById(id);
            managerRepository.delete(manager);
            return true;

        }
        return false;
    }

    public List<Manager> getManagers(Long id) {
        return  managerRepository.findByCompanyId(id);
    }

    public Long getCompanyId(String email) {

        return managerRepository.findCompanyIdByEmail(email);
    }



}
