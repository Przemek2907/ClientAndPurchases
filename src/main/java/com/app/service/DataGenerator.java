package com.app.service;

import com.app.exceptions.MyException;

import java.util.Random;

public class DataGenerator {
    private static Random random = new Random();

    public static String generateString(int len) {
        if (len <= 0) {
            throw new MyException("LEN IS NOT CORRECT");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char)(random.nextInt('Z' - 'A' + 1) + 'A'));
        }
        return sb.toString();
    }

}

