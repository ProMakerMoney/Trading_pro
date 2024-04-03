package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LinearLayout layout = findViewById(R.id.login_anim);
        Animation moveUp = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.move_up);
        Animation moveDown = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.move_down);

        for (int i = 0; i < layout.getChildCount(); i++) {
            // Получаем дочерний элемент
            View child = layout.getChildAt(i);
            if(i%2==0) {
                // Запускаем анимацию для каждого дочернего элемента
                child.startAnimation(moveUp);
            }
            else {
                child.startAnimation(moveDown);
            }
        }

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        register = findViewById(R.id.register);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Успешный вход, обновите UI с информацией о пользователе
                                        Log.d("Auth", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        LinearLayout layout = findViewById(R.id.login_anim);
                                        Animation fadeUp = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_up);
                                        Animation fadeDown = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_down);

                                        // Проходимся по всем дочерним элементам LinearLayout
                                        for (int i = 0; i < layout.getChildCount(); i++) {
                                            // Получаем дочерний элемент
                                            View child = layout.getChildAt(i);
                                            if(i%2==0) {
                                                // Запускаем анимацию для каждого дочернего элемента
                                                child.startAnimation(fadeUp);
                                                child.setBackgroundColor(Color.parseColor("#C7ECCA"));
                                            }
                                            else {
                                                child.startAnimation(fadeDown);
                                                child.setBackgroundColor(Color.parseColor("#C7ECCA"));
                                            }
                                        }

                                        Toast.makeText(LoginActivity.this, "Успешный вход",
                                                Toast.LENGTH_SHORT).show();

                                        // Добавление паузы перед переходом
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, 3000); // Пауза в 2000 миллисекунд (2 секунды)
                                    } else {
                                        // Если вход не удался, выведите сообщение
                                        Log.w("Auth", "signInWithEmail:failure", task.getException());
                                        LinearLayout layout = findViewById(R.id.login_anim);
                                        Animation fadeUp = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.move_up);
                                        Animation fadeDown = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.move_down);
                                        // Проходимся по всем дочерним элементам LinearLayout
                                        for (int i = 0; i < layout.getChildCount(); i++) {
                                            // Получаем дочерний элемент
                                            View child = layout.getChildAt(i);
                                            child.setBackgroundColor(Color.parseColor("#93000A"));

                                        }
                                        Toast.makeText(LoginActivity.this, "Ошибка аутентификации.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Введите email и пароль.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent, куда явно указываем, что хотим перейти из текущего контекста (например, из MainActivity)
                // в RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                // Запускаем активность, используя созданный Intent
                startActivity(intent);
            }
        });
    }
}