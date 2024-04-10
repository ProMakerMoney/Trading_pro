package com.example.trading_pro;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.*;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.service.BybitApiClientFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TradingFragment extends Fragment {
    private TextView textView;
    private ScheduledExecutorService executorService;

    private double lastPrice = 0.0;

    public TradingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trading, container, false);
        textView = rootView.findViewById(R.id.price);

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::fetchPrice, 0, 2, TimeUnit.SECONDS);

        return rootView;
    }

    private void fetchPrice() {
        var client = BybitApiClientFactory.newInstance().newAsyncMarketDataRestClient();
        var tickerRequest = MarketDataRequest.builder().category(CategoryType.LINEAR).symbol("SOLUSDT").build();
        client.getMarketTickers(tickerRequest, System.out::println);
        client.getMarketTickers(tickerRequest, response -> {
            System.out.println(response);
            String priceString = extractLastPrice(response.toString());
            double currentPrice = Double.parseDouble(priceString);

            if (currentPrice > lastPrice) {
                // Цена увеличилась, подсветить зеленым
                highlightTextView(textView, Color.GREEN);
            } else if (currentPrice < lastPrice) {
                // Цена уменьшилась, подсветить красным
                highlightTextView(textView, Color.RED);
            }

            textView.setText(priceString);
            lastPrice = currentPrice;
        });
    }

    public static String extractLastPrice(String input) {
        String regex = "lastPrice=([\\d.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(matcher.find()) {
            return matcher.group(1); // Возвращаем первую найденную группу, где находится значение
        } else {
            return "Not Found";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executorService.shutdown();
    }

    private void highlightTextView(final TextView textView, final int color) {
        textView.setTextColor(color);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Сбросить цвет текста после 500 миллисекунд
                textView.setTextColor(Color.WHITE);
            }
        }, 500);
    }
}