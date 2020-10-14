package com.oss.utils;

import java.util.Random;

public class RandomGenerator {
    public static String generateRandomName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static String generateRandomENodeBId() {
        int min = 0;
        int max = 1048575; // id enodeb </= 1 048 575
        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        StringBuffer buffer = new StringBuffer();
        buffer.append(randomNumber);
        return buffer.toString();
    }

    public static String generateRandomGNodeBId() {
        int min = 0;
        int max = 999999999; // id gnodeb </= 4 294 967 295
        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        StringBuffer buffer = new StringBuffer();
        buffer.append(randomNumber);
        return buffer.toString();
    }

    public static String generateRandomCell5GId() {
        int min = 0;
        int max = 16383; // id cell5g </= 16 383
        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        StringBuffer buffer = new StringBuffer();
        buffer.append(randomNumber);
        return buffer.toString();
    }

    public static String generateRandomCarrierLinkChannel() {
        int min = 0;
        int max = 262143; // DownLink carrier 4G&5G </= 3 279 165
        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        StringBuffer buffer = new StringBuffer();
        buffer.append(randomNumber);
        return buffer.toString();
    }
}
