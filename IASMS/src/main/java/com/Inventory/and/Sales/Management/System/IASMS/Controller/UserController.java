package com.Inventory.and.Sales.Management.System.IASMS.Controller;

import com.Inventory.and.Sales.Management.System.IASMS.Services.CompanyService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ManagerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private HttpServletResponse response;

//    @Autowired
//    private UserService userService;

    @Autowired
    private CompanyService companyService;


    @Autowired
    private ManagerService managerService;

    @GetMapping("/success")
    public void success(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) throws IOException {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");

        if (companyService.isUserExists(email)) {
//            String role = userService.getRole(email);
            response.sendRedirect("http://localhost:4200/admin-dashboard?email="+email+"&name="+name);
        }
        else if (managerService.isUserExists(email)) {
            response.sendRedirect("http://localhost:4200/manager-dashboard");
        }
        else {
            System.out.println("reached backend");
            response.sendRedirect("http://localhost:4200/company-form?email=" + email + "&name=" + name);
//            response.sendRedirect("http://localhost:4200/company-form?email="+ email + "&name=" + name);
        }
    }


//    @PostMapping("/save-role")
//    public ResponseEntity<Map<String, String>> saveRole(@RequestBody Map<String, String> body) {
//        String email = body.get("email");
//        String role = body.get("role");
//        String name=body.get("name");
//        System.out.println("Received request with email: " + email + " and role: " + role+ "name"+name);
//
//        userService.saveUserRole(email, name,role);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("role", role);
//        return ResponseEntity.ok(response);
//    }

}
