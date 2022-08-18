package com.yaroslav.lobur.utils;

import com.yaroslav.lobur.model.entity.enums.Gender;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordEncryptor {
    public static void main(String[] args) {
        String s = PasswordEncryptor.getSHA1String("Password1");
        Pattern pattern = Pattern.compile("^\\d+\\s+(\\p{L}+|\\p{L}+\\s\\p{L}+)$");
        System.out.println("123 Під Голоском".matches(pattern.pattern()));
        System.out.println(s);
        System.out.println(Gender.valueOf(null));
    }
     public static String getSHA1String(String password) {
         if (password == null) return "";
         String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /*private static String getSalt() {
        SecureRandom sr;
        byte[] salt = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            salt = new byte[16];
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException ignored) {}
        return Arrays.toString(salt);
    }*/
}
