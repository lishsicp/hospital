package com.yaroslav.lobur.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

public class VerifyReCaptchaUtil {

    private static final Logger logger = LoggerFactory.getLogger(VerifyReCaptchaUtil.class);

    public static final String SITE_VERIFY_URL = //
            "https://www.google.com/recaptcha/api/siteverify";

    private static final String SITE_KEY = "6LeKHr4hAAAAAE92uKedkBJ8GzyfQcXYeZVdZ3gU";

    public static boolean verify(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }
        try {
            URL verifyUrl = new URL(SITE_VERIFY_URL);
            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String postParams = "secret=" + SITE_KEY
                    + "&response=" + gRecaptchaResponse;
            conn.setDoOutput(true);
            OutputStream outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());
            outStream.flush();
            outStream.close();

            int responseCode = conn.getResponseCode();
            logger.debug("responseCode = {}", responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            Gson gson = new Gson();
            logger.info("Json: {}", sb);
            JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
            boolean success = jsonObject.get("success").getAsBoolean();
            logger.info("response {}", success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
