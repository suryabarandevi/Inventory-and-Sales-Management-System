package com.Inventory.and.Sales.Management.System.IASMS.Controller;


import com.Inventory.and.Sales.Management.System.IASMS.Services.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
public class SessionValidationController {

    @Autowired
    private SessionService sessionService;


    @GetMapping("/api/validate-session")
    public ResponseEntity<Map<String, Object>> getAuthStatus(HttpServletRequest request, @AuthenticationPrincipal OAuth2User principal, HttpServletResponse Response) throws IOException {
        Map<String, Object> response = new HashMap<>();


        boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        response.put("isAuthenticated", isAuthenticated);

        if (isAuthenticated && principal != null) {

            String email = principal.getAttribute("email");
            response.put("email", email);

            if (email != null ) {

                String role = sessionService.getRole(email);

                response.put("role", role);
                if ("home".equals(role)) {
                    Response.sendRedirect("/api/success");
                    return null;
                }

            } else {

                response.put("role", "UNKNOWN");
                System.out.println("User not found: " + email);
            }
        } else {
            response.put("email", null);
            response.put("role", "NONE");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}