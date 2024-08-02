package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatToggleButton;

public class CalcActivity extends Activity {

    private EditText leverageCalc;
    private EditText enterPrice;
    private EditText stopLoss;
    private EditText dol;
    private AppCompatButton calcButton;
    private AppCompatToggleButton myToggleButton;

    private TextView volumeTextView;
    private TextView marginTextView;
    private TextView provisionTextView;
    private TextView tp1TextView;
    private TextView tp2TextView;
    private TextView tp3TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_layout);

        // Инициализация полей
        leverageCalc = findViewById(R.id.leverageCalc);
        enterPrice = findViewById(R.id.enterPrice);
        stopLoss = findViewById(R.id.stopLoss);
        dol = findViewById(R.id.dol);
        calcButton = findViewById(R.id.calc);
        myToggleButton = findViewById(R.id.my_toggle_button);

        // Инициализация TextView для отображения результатов
        volumeTextView = findViewById(R.id.volume);
        marginTextView = findViewById(R.id.margin);
        provisionTextView = findViewById(R.id.provision);
        tp1TextView = findViewById(R.id.tp1);
        tp2TextView = findViewById(R.id.tp2);
        tp3TextView = findViewById(R.id.tp3);

        // Установка слушателя для кнопки
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение значений из EditText
                String leverageCalcValue = leverageCalc.getText().toString();
                String enterPriceValue = enterPrice.getText().toString();
                String stopLossValue = stopLoss.getText().toString();
                String dolValue = dol.getText().toString();

                // Проверка на пустое значение и парсинг чисел
                if (leverageCalcValue.isEmpty() || enterPriceValue.isEmpty() || stopLossValue.isEmpty() || dolValue.isEmpty()) {
                    Toast.makeText(CalcActivity.this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double leverage = Double.parseDouble(leverageCalcValue);
                    double price = Double.parseDouble(enterPriceValue);
                    double stopLoss = Double.parseDouble(stopLossValue);
                    double loss = Double.parseDouble(dolValue);

                    // Получение состояния ToggleButton
                    boolean isLongMode = myToggleButton.isChecked();

                    // Переменные для результатов
                    double deltaStop, delta, provision, margin, volume, TP_1, TP_2, TP_3;

                    if (isLongMode) {
                        // Длинный режим
                        deltaStop = 100 - (stopLoss * 100 / price);
                        delta = deltaStop * leverage;
                        provision = loss * 100 / delta;
                        margin = provision * leverage;
                        volume = margin / price;
                        TP_1 = price + (price * deltaStop / 100);
                        TP_2 = price + (price * deltaStop * 2 / 100);
                        TP_3 = price + (price * deltaStop * 3 / 100);
                    } else {
                        // Короткий режим
                        deltaStop = (stopLoss * 100 / price) - 100;
                        delta = deltaStop * leverage;
                        provision = loss * 100 / delta;
                        margin = provision * leverage;
                        volume = margin / price;
                        TP_1 = price - (price * deltaStop / 100);
                        TP_2 = price - (price * deltaStop * 2 / 100);
                        TP_3 = price - (price * deltaStop * 3 / 100);
                    }

                    // Установка текста в TextView
                    volumeTextView.setText(String.format("Объем: %.4f", volume));
                    marginTextView.setText(String.format("Маржа: %.4f", margin));
                    provisionTextView.setText(String.format("Обеспечение: %.4f", provision));
                    tp1TextView.setText(String.format("TP_1: %.4f", TP_1));
                    tp2TextView.setText(String.format("TP_2: %.4f", TP_2));
                    tp3TextView.setText(String.format("TP_3: %.4f", TP_3));

                    Toast.makeText(CalcActivity.this, "Расчеты выполнены успешно", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(CalcActivity.this, "Ошибка формата числа.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}