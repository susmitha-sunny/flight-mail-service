package com.gotravel.flightmailservice.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.function.Function;

@Service
public class AirlineService {



    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @PostConstruct
    public void setup() {}

    public Boolean sendMail() {

//        System.out.println("Inside lambda");
//        String url = "http://localhost:8081/deleteAirline";
//
//        ResponseEntity<Boolean> response
//                = restTemplate.getForEntity(url, Boolean.class);
//
//        System.out.println("Response from service is " + response.getBody());
//        return response.getBody();

            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

                helper.setFrom("customercare@gotravel.com");
                helper.setTo("user@mail.com");
                helper.setSubject("Reservation cancelled");
                mimeMessage.setContent("You have cancelled your flight reservation. Refund will be initiated within 24 hours. Thank you", "text/html");
                mailSender.send(mimeMessage);
                return Boolean.TRUE;
            } catch (MessagingException e) {
                System.out.println(e);
                return Boolean.FALSE;
            }
        }

    @Bean
    public Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> execute() {
       return apiGatewayProxyRequestEvent -> new APIGatewayProxyResponseEvent().withBody(sendMail().toString());
    }
}
