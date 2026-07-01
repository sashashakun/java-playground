package com.example.fintech.day7.factory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);

        PaymentChannel cardChannel = PaymentChannelFactory.create(ChannelType.CARD);
        String cardPayload = "4111111111111111";
        boolean cardValid = cardChannel.validator().validate(cardPayload);
        System.out.println("Card valid: " + cardValid);
        if (cardValid) {
            System.out.println(cardChannel.processor().process(cardPayload));
        }

        PaymentChannel bankChannel = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);
        String ibanPayload = "DE89370400440532013000";
        boolean bankValid = bankChannel.validator().validate(ibanPayload);
        System.out.println("IBAN valid: " + bankValid);
        if (bankValid) {
            System.out.println(bankChannel.processor().process(ibanPayload));
        }
    }
}
