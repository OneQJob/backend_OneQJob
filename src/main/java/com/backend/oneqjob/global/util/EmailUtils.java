package com.backend.oneqjob.global.util;

public class EmailUtils {

    public static String generateCode() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            stringBuilder.append((int) (Math.random() * 10));
        }

        return stringBuilder.toString();
    }

}
