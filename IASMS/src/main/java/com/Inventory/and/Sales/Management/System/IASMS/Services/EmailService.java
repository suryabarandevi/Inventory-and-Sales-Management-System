package com.Inventory.and.Sales.Management.System.IASMS.Services;

import com.Inventory.and.Sales.Management.System.IASMS.DTO.ProductDTO;
import com.Inventory.and.Sales.Management.System.IASMS.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class EmailService {
//
//    @Autowired
//    private JavaMailSender emailSender;

    @Autowired
    private  OrdersService ordersService;

    @Autowired
    private JavaMailSender mailSender;


public void sendApprovalEmail(String applicantName, String email, String amount, String orderId, String ownerName) throws MessagingException, IOException {
   
    Order order = ordersService.getOrderById(orderId);
    List<ProductDTO> products = parseProducts(order.getProducts());


    PDDocument document = new PDDocument();



    File pdfFile = generateOrderPdf(orderId,products,applicantName,amount,ownerName);


    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    helper.setFrom("suryabarandevi@gmail.com");
    helper.setTo(email);
    helper.setSubject("Order Details");

    String bodyText = "Dear " + applicantName + ",\n\n" +
            "We are excited to let you know that your order has been shipped!\n" +
            "- Total Amount: ₹" + amount + "\n" +
            "- Order ID: " + orderId + "\n" +
            "You’ll receive regular updates about your order status until it is delivered.\n\n" +
            "Thank you for shopping with us!..\n\n" +
            "Best regards,\n" + ownerName;

    helper.setText(bodyText);


    helper.addAttachment(pdfFile.getName(), pdfFile);


    mailSender.send(mimeMessage);


    if (pdfFile.exists()) {
        pdfFile.delete();
    }
}
    private File generateOrderPdf(String orderId, List<ProductDTO> products, String applicantName, String amount,String ownerName) throws IOException {

        File pdfFile = new File("OrderDetails_" + orderId + ".pdf");

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);


            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                PDType1Font titleFont = PDType1Font.HELVETICA_BOLD;
                PDType1Font textFont = PDType1Font.HELVETICA;
                contentStream.beginText();
                contentStream.setFont(titleFont, 16);
                contentStream.setLeading(18f);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Order Details");
                contentStream.newLine();
                contentStream.setFont(textFont, 12);
                contentStream.newLine();


                contentStream.showText("Order ID: " + orderId);
                contentStream.newLine();
                contentStream.showText("Customer Name: " + applicantName);
                contentStream.newLine();
                contentStream.showText("Total Amount: " + amount);
                contentStream.newLine();

                contentStream.newLine();
                contentStream.showText("Items Ordered:");
                contentStream.newLine();

                for (ProductDTO product : products) {
//                    contentStream.showText("Product ID: " + product.getId());
                    //contentStream.newLine();
                    contentStream.showText("-----------------------------------------");
                    contentStream.newLine();
                    contentStream.showText("  -Product Name: " + product.getName());
                    contentStream.newLine();
                    contentStream.showText("  -Quantity: " + product.getQuantity());
                    contentStream.newLine();
                    contentStream.showText("  -Price: " + product.getPrice());
                    contentStream.newLine();
                    contentStream.newLine();
                }


                contentStream.newLine();
                contentStream.showText("Thank you for Ordering!");
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText("With Best Regards");
                contentStream.newLine();
                contentStream.showText(ownerName);

            }


            document.save(pdfFile);
        }

        return pdfFile;
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

    public void sendPaymentEmail(String firstName, String customerEmail, String Amount, String orderId, String Companyname,Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        List<ProductDTO> products = parseProducts(order.getProducts());
        String o="";

        for(ProductDTO productDTO:products){
            o+=productDTO.getName()+" ";
        }
        message.setTo(customerEmail);
        message.setSubject("Order Details");
        message.setText("Dear " + firstName + ",\n\n" +
                "We are excited to inform you that your payment has been received, and your order has been successfully delivered! "+"\n"+
                "-Items Ordered: "+o+".\n"+
                "-Total Amount: ₹"+Amount + ".\n"+
                "Order ID:"+ orderId + ".\n" +

                "\n" +
                "Thank you for shopping with us!..\n\n" +
                "Best regards.\n"+Companyname);
        mailSender.send(message);
    }
}