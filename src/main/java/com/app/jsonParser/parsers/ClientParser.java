package com.app.jsonParser.parsers;

import com.app.model.Client;
import com.app.exceptions.MyException;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class ClientParser implements StringParser<Client> {

    private Client client;

    public ClientParser(Client client) {
        this.client = client;
    }

    @Override
    public String toString(Client client) {

        if (client == null) {
            throw new MyException("STRING PARSER TO STRING EXCEPTION");
        }

        return MessageFormat.format("{0}:{1}:{2}:{3}",
                client.getName(),
                client.getSurname(),
                client.getAge(),
                client.getCash()
        );
    }

    @Override
    public Client fromString(String str) {
        if (str == null || str.isEmpty() && !str.matches(".+:.+:\\d+:(\\d+\\.)*\\d+")) {
            throw new MyException("STRING PARSER FROM STRING EXCEPTION");
        }

        String[] arr = str.split(":");
        return Client.builder()
                .name(arr[0])
                .surname(arr[1])
                .age(Integer.parseInt(arr[2]))
                .cash(new BigDecimal(arr[3]))
                .build();
    }
}
