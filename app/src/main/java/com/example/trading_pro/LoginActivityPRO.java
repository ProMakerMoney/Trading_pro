package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivityPRO extends AppCompatActivity {
    private EditText usernameEditText;  // Изменить имя переменной на username
    private EditText passwordEditText;
    private Button loginButton;
    private TextView notRegisterTextView;
    private CheckBox autoLoginCheckBox;
    private static final String LOGIN_URL = "http://91.226.173.246:8888/authenticate";
    private final OkHttpClient client = new OkHttpClient();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pro);

        usernameEditText = findViewById(R.id.email1);  // Переименовать в соответствии с username
        passwordEditText = findViewById(R.id.textPassword);
        loginButton = findViewById(R.id.button_start);
        notRegisterTextView = findViewById(R.id.notRegister);
        autoLoginCheckBox = findViewById(R.id.auto_login);

        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String savedUsername = preferences.getString("username", null);  // Использовать username
        String savedPassword = preferences.getString("password", null);

        if (savedUsername != null && savedPassword != null) {
            autoLogin(savedUsername, savedPassword);
            return;
        }

        loginButton.setOnClickListener(v -> loginUser());


        notRegisterTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivityPRO.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void autoLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(loginRequest);

        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();
        System.out.println("Автоматический запрос к серверу: " + request);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivityPRO.this, "Auto-login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                System.out.println("Ответ сервера при авто-логине: " + responseBody);

                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    JwtResponse jwtResponse = gson.fromJson(responseBody, JwtResponse.class);
                    saveToken(jwtResponse.getToken());
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivityPRO.this, "Auto-login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivityPRO.this, NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivityPRO.this, "Auto-login failed: " + responseBody, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();  // Использовать username
        String password = passwordEditText.getText().toString();

        LoginRequest loginRequest = new LoginRequest(username, password);
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(loginRequest);

        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();
        System.out.println("Запрос к серверу: " + request);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivityPRO.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                System.out.println("Ответ сервера: " + responseBody);

                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    JwtResponse jwtResponse = gson.fromJson(responseBody, JwtResponse.class);
                    System.out.println("Ответ: " + jwtResponse);
                    System.out.println("token: " + jwtResponse.getToken());
                    saveToken(jwtResponse.getToken());
                    if (autoLoginCheckBox.isChecked()) {
                        saveLoginDetails(username, password);
                    }
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivityPRO.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivityPRO.this, NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivityPRO.this, "Login failed: " + responseBody, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void saveLoginDetails(String username, String password) {  // Использовать username
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);  // Сохранить username
        editor.putString("password", password);
        editor.apply();
    }

    // Классы для JSON запросов и ответов
    private static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public class JwtResponse {
        @SerializedName("jwt")
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "JwtResponse{" +
                    "token='" + token + '\'' +
                    '}';
        }
    }
}