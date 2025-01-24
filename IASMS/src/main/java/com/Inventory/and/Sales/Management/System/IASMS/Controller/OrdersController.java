package com.Inventory.and.Sales.Management.System.IASMS.Controller;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.OrderDTO;
import com.Inventory.and.Sales.Management.System.IASMS.DTO.ProductDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Company;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Order;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Product;
import com.Inventory.and.Sales.Management.System.IASMS.Services.CompanyService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ManagerService;

import com.Inventory.and.Sales.Management.System.IASMS.Services.OrdersService;
import com.Inventory.and.Sales.Management.System.IASMS.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200",allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private CompanyService companyService;

//    @GetMapping("/all")
//    public ResponseEntity<List<Order>> getAllOrders() {
//        List<Order> orders = ordersService.getAllOrders();
//        return ResponseEntity.ok(orders);
//    }

    @PutMapping("/updateStatus/{orderId}/{status}")
    public ResponseEntity<Boolean> updateOrderStatus(@PathVariable String orderId,@PathVariable String status) {
        Order order=ordersService.getOrderById(orderId);
        order.setStatus(status);
        ordersService.saveOrder(order);

        return ResponseEntity.ok(true);
    }


    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody Map<String, Object> request,@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long companyId=0L;
        if(companyService.isUserExists(email)){
            companyId=companyService.getCompanyId(email);
        }
        else if(managerService.isUserExists(email)){
            companyId= managerService.getCompanyId(email);
        }

        Company company = companyService.getCompanyById(companyId).getBody();


        String status = (String) request.get("status");
        List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");

        LocalDateTime date = LocalDateTime.now();

        Order order = new Order();

        order.setStatus(status);
        order.setDate(date);
        order.setCompany(company);

        List<ProductDTO> products = new ArrayList<>();
        Double amount=0.0;

        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            Integer quantity = Integer.valueOf(item.get("quantity").toString());

            Product product = productService.getProductbyId(productId);
            if (product == null) {
                return ResponseEntity.badRequest().body(null);
            }


            if (product.getQuantity() < quantity) {
                return ResponseEntity.badRequest().body(null);
            }

            System.out.println(quantity);
            System.out.println(quantity*product.getPrice());
            product.setQuantity(product.getQuantity() - quantity);
            products.add(new ProductDTO(productId,product.getName(),product.getCategory().getId(),quantity,quantity*product.getPrice()));
            System.out.println(quantity);
            System.out.println(quantity*product.getPrice());
            amount+=quantity*product.getPrice();

            productService.updateProduct(productId, product);
        }


        order.setProducts(stringForm(products));
        order.setProductPrice(amount);
        order.setQuantity(products.size());

        ordersService.saveOrder(order);

        List<ProductDTO> productDetails = products;
        OrderDTO orderDTO = new OrderDTO(
                order.getOrderId(),

                amount,
                order.getStatus(),
                order.getQuantity(),
                order.getDate(),
                productDetails
        );

        return ResponseEntity.ok(orderDTO);
    }

    private String stringForm(List<ProductDTO> products) {
        StringBuilder productDetails = new StringBuilder();
        for (ProductDTO productDTO : products) {
            productDetails.append(productDTO.toString()).append("; ");
        }
        return productDetails.toString();
    }

    @GetMapping("/company")
    public ResponseEntity<List<OrderDTO>> getOrdersByCompany(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long authenticatedCompanyId = 0L;

        if (companyService.isUserExists(email)) {
            authenticatedCompanyId = companyService.getCompanyId(email);
        } else if (managerService.isUserExists(email)) {
            authenticatedCompanyId = managerService.getCompanyId(email);
        }


        List<Order> orders = ordersService.getOrdersByCompanyId(authenticatedCompanyId);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        if(orders.size()==0)return ResponseEntity.ok(orderDTOList);
        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // No orders found
        }


        for (Order order : orders) {
            List<ProductDTO> products = parseProducts(order.getProducts());
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderId(),
                    order.getProductPrice(),
                    order.getStatus(),
                    order.getQuantity(),
                    order.getDate(),
                    products
            );
            orderDTOList.add(orderDTO);
        }

        return ResponseEntity.ok(orderDTOList);
    }


    private List<ProductDTO> parseProducts(String productsString) {
        List<ProductDTO> products = new ArrayList<>();
        if (productsString != null && !productsString.isEmpty()) {
            String[] productArray = productsString.split("; ");
            for (String productDetails : productArray) {
                ProductDTO productDTO = parseProductDTOFromString(productDetails);
                products.add(productDTO);
            }
        }
        return products;
    }

    private ProductDTO parseProductDTOFromString(String productString) {
        String[] parts = productString.replace("ProductDTO{", "").replace("}", "").split(", ");
        Long id = Long.parseLong(parts[0].split("=")[1]);
        String name = parts[1].split("=")[1];
        Long categoryId = Long.parseLong(parts[2].split("=")[1]);
        Integer quantity = Integer.parseInt(parts[3].split("=")[1]);
        Double price = Double.parseDouble(parts[4].split("=")[1]);

        return new ProductDTO(id, name, categoryId, quantity, price);
    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable String id) {
        Order order=ordersService.getOrderById(id);
        List<ProductDTO> products = parseProducts(order.getProducts());

        ordersService.deleteOrder(order,products);
        return ResponseEntity.ok(true);
    }


    @GetMapping("/noOfOrders")
    public ResponseEntity<Map<String,String>> noOfOrders(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        Map<String, String> h = new HashMap<>();
        h.put("Name", name);
        h.put("email", email);
        Long companyId = 0L;

        if (managerService.isUserExists(email)) {
            h.put("Role", "Manager");
            companyId = managerService.getCompanyId(email);
        } else if (companyService.isUserExists(email)) {
            h.put("Role", "Owner");
            companyId = companyService.getCompanyId(email);
        }

        int noOfOrders = ordersService.getNoOfOrder(companyId);
        h.put("Orders", String.valueOf(noOfOrders));
        return ResponseEntity.ok(h);

    }
}
