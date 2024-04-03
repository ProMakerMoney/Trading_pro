package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivityPRO extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pro);

        emailEditText = findViewById(R.id.email1);
        passwordEditText = findViewById(R.id.textPassword);
        mAuth = FirebaseAuth.getInstance();
        TextView notRegisterTextView = findViewById(R.id.notRegister);

        PopupWindow popupWindow = new PopupWindow(
                LayoutInflater.from(this).inflate(R.layout.popup_layout, null),
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        PopupWindow popupWindow2 = new PopupWindow(
                LayoutInflater.from(this).inflate(R.layout.popup_layout2, null),
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        LottieAnimationView loadingAnimation = popupWindow.getContentView().findViewById(R.id.loading_animation);
        Button button = findViewById(R.id.button_start);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();




                if(isVpnEnabled()){
                    popupWindow.showAtLocation(
                            findViewById(android.R.id.content),
                            Gravity.CENTER,
                            0,
                            0
                    );
                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivityPRO.this, new OnCompleteListener<AuthResult>() {
                                    @SuppressLint("ResourceAsColor")
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Успешный вход, обновите UI с информацией о пользователе
                                            Log.d("Auth", "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_green));
                                            }


                                            Toast.makeText(LoginActivityPRO.this, "Успешный вход",
                                                    Toast.LENGTH_SHORT).show();
                                            popupWindow.dismiss();
                                            popupWindow2.showAtLocation(
                                                    findViewById(android.R.id.content),
                                                    Gravity.CENTER,
                                                    0,
                                                    0
                                            );

                                            // Добавление паузы перед переходом
                                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    popupWindow.dismiss();
                                                    popupWindow2.showAtLocation(
                                                            findViewById(android.R.id.content),
                                                            Gravity.CENTER,
                                                            0,
                                                            0
                                                    );
                                                    Intent intent = new Intent(LoginActivityPRO.this, NavigationActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }, 4000); // Пауза в 2000 миллисекунд (2 секунды)
                                        } else {
                                            // Если вход не удался, выведите сообщение
                                            Log.w("Auth", "signInWithEmail:failure", task.getException());
                                            LinearLayout layout = findViewById(R.id.login_anim);
                                            android.view.animation.Animation fadeUp = AnimationUtils.loadAnimation(LoginActivityPRO.this, R.anim.move_up);
                                            Animation fadeDown = AnimationUtils.loadAnimation(LoginActivityPRO.this, R.anim.move_down);
                                            // Проходимся по всем дочерним элементам LinearLayout
                                            for (int i = 0; i < layout.getChildCount(); i++) {
                                                // Получаем дочерний элемент
                                                View child = layout.getChildAt(i);
                                                child.setBackgroundColor(Color.parseColor("#93000A"));

                                            }
                                            Toast.makeText(LoginActivityPRO.this, "Ошибка аутентификации.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivityPRO.this, "Введите email и пароль.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivityPRO.this, "VPN выключен!", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.md_theme_dark_errorContainer));
                    }
                }

            }
        });

        notRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск новой активности для регистрации
                Intent intent = new Intent(LoginActivityPRO.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isVpnEnabled() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
    }

}
