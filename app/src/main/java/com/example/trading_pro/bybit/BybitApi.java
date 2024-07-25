package com.example.trading_pro.bybit;




import static java.sql.Types.TIMESTAMP;

import androidx.annotation.NonNull;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.*;
import com.bybit.api.client.domain.position.*;
import com.bybit.api.client.domain.position.request.*;
import com.bybit.api.client.service.BybitApiClientFactory;



import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class BybitApi {
    private final String BASE_URL;
    final static String API_KEY = "bvQRWwQU8QapNl3Ppl";
    final static String API_SECRET = "P5h8tnabkftRGzrdFV4DXbggI7XJnaaXx6KY";
    private final Gson gson;
    final static String RECV_WINDOW = "5000";

    public BybitApi() {
        this.BASE_URL = "https://api.bybit.com";
        this.gson = new Gson();
    }

    /**
     * Возвращает текущий таймстамп в миллисекундах.
     *
     * @return текущий таймстамп в миллисекундах
     */
    private String getTimestamp() {
        return Long.toString(Instant.now().toEpochMilli());
    }

    private String generateSignature(String timestamp, String queryString, String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {
        String recvWindow = "5000";
        String payload = timestamp + API_KEY + recvWindow + requestBody;
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(API_SECRET.getBytes(), "HmacSHA256");
        sha256Hmac.init(secretKeySpec);
        return bytesToHex(sha256Hmac.doFinal(payload.getBytes()));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public void getOpenPositions( ){
        var client = BybitApiClientFactory.newInstance(API_KEY, API_SECRET, BybitApiConfig.MAINNET_DOMAIN).newAsyncPositionRestClient();
        var positionListRequest = PositionDataRequest.builder().category(CategoryType.LINEAR).symbol(null).settleCoin("USDT").build();
        client.getPositionInfo(positionListRequest, System.out::println);
    }


    /**
     * To generate query string for GET requests
     * @param map
     * @return
     */
    private static StringBuilder genQueryStr(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key)
                    .append("=")
                    .append(map.get(key))
                    .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb;
    }

    /**
     * The way to generate the sign for GET requests
     * @param params: Map input parameters
     * @return signature used to be a parameter in the header
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static String genGetSign(Map<String, Object> params) throws NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder sb = genQueryStr(params);
        String queryStr = TIMESTAMP + API_KEY + RECV_WINDOW + sb;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(API_SECRET.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return bytesToHex(sha256_HMAC.doFinal(queryStr.getBytes()));
    }
}