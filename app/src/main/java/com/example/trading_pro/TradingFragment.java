package com.example.trading_pro;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.*;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private TextView statusServerOne;
    private ScheduledExecutorService executorService;

    private LottieAnimationView lottieAnimationView;

    private double lastPrice = 0.0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        statusServerOne = rootView.findViewById(R.id.server_status);
        lottieAnimationView = rootView.findViewById(R.id.animation_status_1);

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::fetchPrice, 0, 2, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(this::serverStatus, 0, 7, TimeUnit.SECONDS);

        return rootView;
    }

    private void serverStatus(){
        CollectionReference statusCollection = db.collection("servers")
                .document("server_one")
                .collection("status");

        System.out.println(statusCollection.getFirestore());

        statusCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot documents = task.getResult();
                            LocalDateTime maxDateTime = null;
                            for (DocumentSnapshot document : documents) {
                                String dateTimeString = document.getString("Status");
                                LocalDateTime dateTime = parseDateTime(dateTimeString);
                                if (maxDateTime == null || dateTime.isAfter(maxDateTime)) {
                                    maxDateTime = dateTime;
                                }
                            }
                            if (maxDateTime != null) {
                                // Используйте полученное значение maxDateTime
                                LocalDateTime currentDateTime = LocalDateTime.now();
                                // Вычисляем разницу между текущим временем и максимальным временем из документов
                                Duration duration = Duration.between(maxDateTime, currentDateTime);

                                // Проверяем, если разница больше 2 минут
                                if (duration.toMinutes() > 1) {
                                    // Выводим разницу
                                    statusServerOne.setText("Сервер отдыхает: " + duration.toMinutes() + " м.");
                                    lottieAnimationView.setAnimation("server_off.json");
                                    lottieAnimationView.playAnimation();
                                }
                                else {
                                    statusServerOne.setText("Сервер пашет");
                                    lottieAnimationView.setAnimation("work_on.json");
                                    lottieAnimationView.playAnimation();
                                }
                            } else {
                                // Коллекция пустая
                            }
                        } else {
                            // Ошибка получения документов
                        }
                    }
                });
    }

    private void fetchPrice() {
        var client = BybitApiClientFactory.newInstance().newAsyncMarketDataRestClient();
        var tickerRequest = MarketDataRequest.builder().category(CategoryType.LINEAR).symbol("SOLUSDT").build();
        //client.getMarketTickers(tickerRequest, System.out::println);
        client.getMarketTickers(tickerRequest, response -> {
            //System.out.println(response);
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

    private LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}