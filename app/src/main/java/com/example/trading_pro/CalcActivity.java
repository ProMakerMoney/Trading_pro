package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatToggleButton;

import java.util.Arrays;
import java.util.List;

public class CalcActivity extends Activity {

//    private EditText leverageCalc;
//    private EditText enterPrice;
//    private EditText stopLoss;
//    private EditText dol;
    private AppCompatButton calcButton;


    private AutoCompleteTextView autoCompleteTextView;
    private TextView leverageTextView;

    private AppCompatToggleButton toggleButton;
    private Button buttonOpenLong;
    private Button buttonOpenShort;

//    private TextView volumeTextView;
//    private TextView marginTextView;
//    private TextView provisionTextView;
//    private TextView tp1TextView;
//    private TextView tp2TextView;
//    private TextView tp3TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_layout);

        leverageTextView = findViewById(R.id.leverage);

        leverageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeverageDialog();
            }
        });


        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        // Predefined list of coins
        List<String> coins = Arrays.asList("BTC", "ETH", "LTC", "LINK", "ADA", "AAVE");

        // Set up ArrayAdapter for AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, coins);
        autoCompleteTextView.setAdapter(adapter);

        // Optional: Set threshold for showing suggestions
        autoCompleteTextView.setThreshold(1); // Start showing suggestions after one character input
        // Инициализация полей
//        leverageCalc = findViewById(R.id.leverageCalc);
//        enterPrice = findViewById(R.id.enterPrice);
//        stopLoss = findViewById(R.id.stopLoss);
//        dol = findViewById(R.id.dol);
        calcButton = findViewById(R.id.calc);
        toggleButton = findViewById(R.id.my_toggle_button);
        buttonOpenLong = findViewById(R.id.button_open_long);
        buttonOpenShort = findViewById(R.id.button_open_short);

        // Инициализация TextView для отображения результатов
//        volumeTextView = findViewById(R.id.volume);
//        marginTextView = findViewById(R.id.margin);
//        provisionTextView = findViewById(R.id.provision);
//        tp1TextView = findViewById(R.id.tp1);
//        tp2TextView = findViewById(R.id.tp2);
//        tp3TextView = findViewById(R.id.tp3);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is enabled (Long position)
                    buttonOpenLong.setBackgroundColor(Color.parseColor("#4CAF50")); // Original green color
                    buttonOpenShort.setBackgroundColor(Color.GRAY); // Gray color
                } else {
                    // Toggle is disabled (Short position)
                    buttonOpenLong.setBackgroundColor(Color.GRAY); // Gray color
                    buttonOpenShort.setBackgroundColor(Color.parseColor("#F44336")); // Original red color
                }
            }
        });

        // Initialize the state based on the toggle button
        if (toggleButton.isChecked()) {
            buttonOpenLong.setBackgroundColor(Color.parseColor("#4CAF50"));
            buttonOpenShort.setBackgroundColor(Color.GRAY);
        } else {
            buttonOpenLong.setBackgroundColor(Color.GRAY);
            buttonOpenShort.setBackgroundColor(Color.parseColor("#F44336"));
        }

        // Установка слушателя для кнопки
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение значений из EditText
//                String leverageCalcValue = leverageCalc.getText().toString();
//                String enterPriceValue = enterPrice.getText().toString();
//                String stopLossValue = stopLoss.getText().toString();
//                String dolValue = dol.getText().toString();

                // Проверка на пустое значение и парсинг чисел
//                if (leverageCalcValue.isEmpty() || enterPriceValue.isEmpty() || stopLossValue.isEmpty() || dolValue.isEmpty()) {
//                    Toast.makeText(CalcActivity.this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                try {
//                    double leverage = Double.parseDouble(leverageCalcValue);
//                    double price = Double.parseDouble(enterPriceValue);
//                    double stopLoss = Double.parseDouble(stopLossValue);
//                    double loss = Double.parseDouble(dolValue);

//                    // Получение состояния ToggleButton
//                    boolean isLongMode = myToggleButton.isChecked();
//
//                    // Переменные для результатов
//                    double deltaStop, delta, provision, margin, volume, TP_1, TP_2, TP_3;
//
//                    if (isLongMode) {
//                        // Длинный режим
//                        deltaStop = 100 - (stopLoss * 100 / price);
//                        delta = deltaStop * leverage;
//                        provision = loss * 100 / delta;
//                        margin = provision * leverage;
//                        volume = margin / price;
//                        TP_1 = price + (price * deltaStop / 100);
//                        TP_2 = price + (price * deltaStop * 2 / 100);
//                        TP_3 = price + (price * deltaStop * 3 / 100);
//                    } else {
//                        // Короткий режим
//                        deltaStop = (stopLoss * 100 / price) - 100;
//                        delta = deltaStop * leverage;
//                        provision = loss * 100 / delta;
//                        margin = provision * leverage;
//                        volume = margin / price;
//                        TP_1 = price - (price * deltaStop / 100);
//                        TP_2 = price - (price * deltaStop * 2 / 100);
//                        TP_3 = price - (price * deltaStop * 3 / 100);
//                    }

                    // Установка текста в TextView
//                    volumeTextView.setText(String.format("Объем: %.4f", volume));
//                    marginTextView.setText(String.format("Маржа: %.4f", margin));
//                    provisionTextView.setText(String.format("Обеспечение: %.4f", provision));
//                    tp1TextView.setText(String.format("TP_1: %.4f", TP_1));
//                    tp2TextView.setText(String.format("TP_2: %.4f", TP_2));
//                    tp3TextView.setText(String.format("TP_3: %.4f", TP_3));

                    Toast.makeText(CalcActivity.this, "Расчеты выполнены успешно", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(CalcActivity.this, "Ошибка формата числа.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showLeverageDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_leverage);

        final EditText editTextLeverage = dialog.findViewById(R.id.editTextLeverage);
        final SeekBar seekBarLeverage = dialog.findViewById(R.id.seekBarLeverage);
        Button buttonOk = dialog.findViewById(R.id.buttonOk);

        // Initialize the seekbar and edittext with the current value
        int currentLeverage = Integer.parseInt(leverageTextView.getText().toString().replace("X", ""));
        seekBarLeverage.setProgress(currentLeverage);
        editTextLeverage.setText(String.valueOf(currentLeverage));

        seekBarLeverage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editTextLeverage.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int leverage = Integer.parseInt(editTextLeverage.getText().toString());
                leverageTextView.setText(leverage + "X");
                dialog.dismiss();
            }
        });

        // Set dialog size
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();
    }
}