package com.destaxa.client.controller;

import com.destaxa.client.ISO8583Message;
import com.destaxa.client.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.Socket;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final ValidationService validationService;

    public PaymentController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/authorize")
    public ResponseEntity<String> authorizePayment(@RequestBody Map<String, String> payload) {
        ResponseEntity<String> validationError = validationService.validateRequest(payload);
        if (validationError != null) {
            return validationError;
        }

        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String cardNumber = payload.get("card_number");
            String value = payload.get("value");

            ISO8583Message requestMessage = new ISO8583Message("0200");
            requestMessage.setField(2, cardNumber);
            requestMessage.setField(4, value);
            requestMessage.setField(11, "123456");

            logger.info("Enviando mensagem ISO8583 ao servidor: {}", requestMessage.buildMessage());
            output.println(requestMessage.buildMessage());

            String rawResponse = input.readLine();
            logger.info("Resposta recebida do servidor: {}", rawResponse);

            ISO8583Message responseMessage = ISO8583Message.parseMessage(rawResponse);
            return ResponseEntity.ok("Response Code: " + responseMessage.getField(39));

        } catch (IOException e) {
            logger.error("Erro de comunicação com o servidor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro de comunicação com o servidor.");
        }
    }
}
