package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;


public class StartActivity extends Activity {

    AppCompatButton calc;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button button = findViewById(R.id.button_start);
        calc = findViewById(R.id.calculatorB);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для перехода на новое Activity
                Intent intent = new Intent(StartActivity.this, LoginActivityPRO.class);
                startActivity(intent);
            }
        });

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(StartActivity.this, "Калькулятор", Toast.LENGTH_SHORT).show();
                // Создаем Intent для перехода на новое Activity
                Intent intent = new Intent(StartActivity.this, CalcActivity.class);
                startActivity(intent);
            }
        });

    }

}
