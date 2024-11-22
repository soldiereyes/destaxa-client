package com.destaxa.client.controller;


import com.destaxa.client.ISO8583Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping("/authorize")
    public String authorizePayment(@RequestBody Map<String, String> payload) {
        logger.info("Received payment authorization request with payload: {}", payload);

        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            ISO8583Message requestMessage = new ISO8583Message("0200");
            requestMessage.setField(2, payload.get("card_number"));
            requestMessage.setField(4, payload.get("value"));
            requestMessage.setField(11, "123456");

            logger.debug("Sending ISO8583 request: {}", requestMessage.buildMessage());
            output.println(requestMessage.buildMessage());

            String rawResponse = input.readLine();
            logger.debug("Raw response received: {}", rawResponse);

            ISO8583Message responseMessage = ISO8583Message.parseMessage(rawResponse);
            logger.info("Transaction processed with Response Code: {}", responseMessage.getField(39));

            return "Response Code: " + responseMessage.getField(39);

        } catch (IOException e) {
            logger.error("Error communicating with the server: {}", e.getMessage());
            return "Error communicating with the server.";
        }
    }
}

