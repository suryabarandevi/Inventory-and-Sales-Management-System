package com.Inventory.and.Sales.Management.System.IASMS.Controller;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.ManagerDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Manager;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CompanyService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200",allowCredentials = "true")
@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;


    @Autowired
    private CompanyService companyService;


    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.getAllManagers();
    }

    @GetMapping("/company")
    public ResponseEntity<List<ManagerDTO>> getManagersByCompanyId(@AuthenticationPrincipal OAuth2User principal) {
        String email=principal.getAttribute("email");
        Long Id=companyService.getCompanyId(email);

        List<ManagerDTO> managerDTOS=new ArrayList<>();

        List<Manager> managers = managerService.getManagers(Id);
        if(managers.size()==0){
            return ResponseEntity.ok(managerDTOS);
        }
        for(Manager manager:managers){
            ManagerDTO managerDTO=new ManagerDTO(manager.getId(),manager.getName(),manager.getEmail(),manager.getPhone());
            managerDTOS.add(managerDTO);
        }

        if (managers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(managerDTOS);
    }




    @PostMapping("/create")
    public ResponseEntity<ManagerDTO> createManager(
            @RequestBody Manager manager,
            @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }

        String email = principal.getAttribute("email");
        if(managerService.isUserExists(manager.getEmail())){
            return ResponseEntity.badRequest().build();
        }
        Manager createdManager = managerService.createManager(manager, email);
        ManagerDTO managerDTO=new ManagerDTO(createdManager.getId(),createdManager.getName(),createdManager.getEmail(),createdManager.getPhone());

        if (createdManager != null) {
            return ResponseEntity.ok(managerDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateManager(@PathVariable Long id, @RequestBody ManagerDTO managerDetails) {

//        String email=principal.getAttribute("email");
        Manager manager=managerService.getManagerById(id);
        manager.setName(managerDetails.getName());
        manager.setEmail(managerDetails.getEmail());
        manager.setPhone(managerDetails.getPhone());
        managerService.updateManager(id, manager);

        return ResponseEntity.ok(true);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return ResponseEntity.ok(true);
    }



}
