package com.destaxa.client.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/authorize")
    public String authorizePayment(@RequestBody String message) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            output.println(message);
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error communicating with the server.";
        }
    }
}

