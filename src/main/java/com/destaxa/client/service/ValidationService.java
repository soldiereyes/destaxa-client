package com.destaxa.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    public ResponseEntity<String> validateRequest(Map<String, String> payload) {
        if (!payload.containsKey("card_number")) {
            logger.warn("Requisição inválida: Campo 'card_number' ausente.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo 'card_number' é obrigatório.");
        }

        if (!payload.containsKey("value")) {
            logger.warn("Requisição inválida: Campo 'value' ausente.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo 'value' é obrigatório.");
        }

        String cardNumber = payload.get("card_number");
        String value = payload.get("value");

        if (!cardNumber.matches("\\d{16}")) {
            logger.warn("Número do cartão inválido: {}", cardNumber);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Número do cartão deve conter 16 dígitos numéricos.");
        }

        try {
            double transactionValue = Double.parseDouble(value);
            if (transactionValue == 0) {
                logger.warn("Valor da transação inválido: {}", value);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valor da transação não pode ser zero.");
            }
            if (transactionValue > 1000.00) {
                logger.warn("Valor acima do permitido: {}", transactionValue);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valor da transação não pode ultrapassar 1000.00.");
            }
        } catch (NumberFormatException e) {
            logger.warn("Formato inválido para o campo 'value': {}", value);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O campo 'value' deve ser numérico.");
        }

        return null;
    }
}
