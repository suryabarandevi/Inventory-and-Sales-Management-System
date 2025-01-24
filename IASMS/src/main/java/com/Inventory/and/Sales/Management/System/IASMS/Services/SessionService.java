package com.Inventory.and.Sales.Management.System.IASMS.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {


    @Autowired
    private CompanyService companyService;

    @Autowired
    private ManagerService managerService;

    private static final Map<String, String> sessionStore = new HashMap<>();



     public String getRole(String email){
         if(companyService.isUserExists(email)){
             return "admin";
         }
         else if(managerService.isUserExists(email)){
             return "manager";
         }
         else{
             return "home";
         }


     }


    public void createSession(String sessionId, String userId) {
        sessionStore.put(sessionId, userId);
    }
}
