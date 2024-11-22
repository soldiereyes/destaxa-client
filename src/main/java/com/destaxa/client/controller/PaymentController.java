package com.destaxa.client.controller;


import com.destaxa.shared.ISO8583Message;
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

    @PostMapping("/authorize")
    public String authorizePayment(@RequestBody Map<String, String> payload) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            ISO8583Message requestMessage = new ISO8583Message("0200");
            requestMessage.setField(2, payload.get("card_number"));
            requestMessage.setField(4, payload.get("value"));
            requestMessage.setField(11, "123456");

            output.println(requestMessage.buildMessage());
            String rawResponse = input.readLine();

            ISO8583Message responseMessage = ISO8583Message.parseMessage(rawResponse);
            return "Response Code: " + responseMessage.getField(39);

        } catch (IOException e) {
            e.printStackTrace();
            return "Error communicating with the server.";
        }
    }
}

