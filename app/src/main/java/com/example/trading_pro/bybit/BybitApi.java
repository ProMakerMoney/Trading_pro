package com.example.trading_pro.bybit;




import com.google.gson.Gson;
import okhttp3.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


public class BybitApi {
    private final String BASE_URL;
    private final String API_KEY;
    private final String API_SECRET;
    private final OkHttpClient client;
    private final Gson gson;

    public BybitApi(String apiKey, String apiSecret, boolean isTestnet) {
        this.BASE_URL = isTestnet ? "https://api-testnet.bybit.com" : "https://api.bybit.com";
        this.API_KEY = apiKey;
        this.API_SECRET = apiSecret;
        this.client = new OkHttpClient().newBuilder().build();
        this.gson = new Gson();
    }

    private String getTimestamp() {
        return Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
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

    public void placeOrder(String symbol, String side, String orderType, String qty, String price) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String timestamp = getTimestamp();
        String endpoint = "/v5/order/create";
        String url = BASE_URL + endpoint;

        Map<String, Object> params = new HashMap<>();
        params.put("category", "linear");
        params.put("symbol", symbol);
        params.put("side", side);
        params.put("orderType", orderType);
        params.put("qty", qty);
        params.put("price", price);
        params.put("timeInForce", "GTC");

        String jsonParams = gson.toJson(params);
        String signature = generateSignature(timestamp, "", jsonParams);

        RequestBody body = RequestBody.create(jsonParams, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("X-BAPI-API-KEY", API_KEY)
                .addHeader("X-BAPI-SIGN", signature)
                .addHeader("X-BAPI-SIGN-TYPE", "2")
                .addHeader("X-BAPI-TIMESTAMP", timestamp)
                .addHeader("X-BAPI-RECV-WINDOW", "5000")
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Order placed successfully: " + response.body().string());
                } else {
                    System.err.println("Order placement failed: " + response.body().string());
                }
            }
        });
    }

    public void getOpenOrder(String symbol) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String timestamp = getTimestamp();
        String endpoint = "/v5/order/realtime";
        String url = BASE_URL + endpoint;

        Map<String, Object> params = new HashMap<>();
        params.put("category", "linear");
        params.put("symbol", symbol);
        params.put("settleCoin", "USDT");

        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        queryString.deleteCharAt(queryString.length() - 1);

        String signature = generateSignature(timestamp, queryString.toString(), "");

        Request request = new Request.Builder()
                .url(url + "?" + queryString.toString())
                .get()
                .addHeader("X-BAPI-API-KEY", API_KEY)
                .addHeader("X-BAPI-SIGN", signature)
                .addHeader("X-BAPI-SIGN-TYPE", "2")
                .addHeader("X-BAPI-TIMESTAMP", timestamp)
                .addHeader("X-BAPI-RECV-WINDOW", "5000")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Open orders: " + response.body().string());
                } else {
                    System.err.println("Failed to get open orders: " + response.body().string());
                }
            }
        });
    }
}