package app.util;

import app.entities.*;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;

public class EmailUtil {
    public static void sendOffer(Customer customer, Order order) throws IOException {
        String templateId = "d-0b8c036641524d78beb134d9cd154dd3";
        String baseUrl = System.getenv("BASE_URL");

        Personalization personalization = new Personalization();
        personalization.addTo(new Email(customer.getEmail()));
        personalization.addDynamicTemplateData("customer_name", customer.getFullname());
        personalization.addDynamicTemplateData("offer_link", baseUrl + "/orders/" + order.getId() + "/offer");

        send(templateId, personalization);
    }

    public static void sendOrderConfirmation(Customer customer) throws IOException {
        String templateId = "d-d85e43bab16e459b866e478b61bf1e92";

        Personalization personalization = new Personalization();
        personalization.addTo(new Email(customer.getEmail()));
        personalization.addDynamicTemplateData("customer_name", customer.getFullname());

        send(templateId, personalization);
    }

    private static void send(String templateId, Personalization personalization) throws IOException {
        Email from = new Email("noreply.johannes.fog@gmail.com");
        from.setName("Johannes Fog Byggemarked");

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            mail.templateId = templateId;
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println("Error sending mail");
            throw ex;
        }
    }
}