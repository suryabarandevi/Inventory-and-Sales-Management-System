package com.Inventory.and.Sales.Management.System.IASMS.Controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*",allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class CurrentUser {




    @GetMapping("/current-user/email")
    public String getCurrentUserEmail(@AuthenticationPrincipal OAuth2User principal) {
//        String email = currentUserService.getCurrentUserEmail();
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        System.out.println(email);
        if (email != null) {
            return email;
        } else {
            return "No authenticated user found";
        }
    }
}
