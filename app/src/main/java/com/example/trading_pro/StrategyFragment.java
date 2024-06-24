package com.example.trading_pro;





import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.trading_pro.bybit.BybitApi;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StrategyFragment extends Fragment {

    private BybitApi bybitApi;

    public StrategyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация BybitApi с вашими API ключами
        bybitApi = new BybitApi("bvQRWwQU8QapNl3Ppl", "P5h8tnabkftRGzrdFV4DXbggI7XJnaaXx6KY", false); // true для тестнета
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_strategy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Найти кнопки
        AppCompatButton buyButton = view.findViewById(R.id.buy);
        AppCompatButton sellButton = view.findViewById(R.id.sell);

        // Установить слушатели кликов для кнопок
        buyButton.setOnClickListener(v -> {
            // Логика для кнопки BUY
            if (isAdded() && getContext() != null) { // Проверяем, прикреплен ли фрагмент и не равен ли контекст null
                Toast.makeText(getContext(), "BUY", Toast.LENGTH_SHORT).show();
                // Размещение рыночного ордера на покупку
                try {
                    //bybitApi.placeOrder("SOLUSDT", "Buy", "Market", "0.1", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sellButton.setOnClickListener(v -> {
            // Логика для кнопки SELL
            if (isAdded() && getContext() != null) { // Проверяем, прикреплен ли фрагмент и не равен ли контекст null
                Toast.makeText(getContext(), "SELL", Toast.LENGTH_SHORT).show();
                // Размещение рыночного ордера на продажу
                try {
                    //bybitApi.placeOrder("SOLUSDT", "Sell", "Market", "0.1", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}