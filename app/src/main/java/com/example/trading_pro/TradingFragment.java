package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trading_pro.coin.Coin;
import com.example.trading_pro.coin.CoinAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TradingFragment extends Fragment {

    private TextView statusServerOne;
    private LinearLayout layoutServerOne;
    private RecyclerView recyclerView;
    private Handler handler;
    private OkHttpClient client;
    private CoinAdapter coinAdapter;
    private List<Coin> coinList;
    private String urlStatus = "http://91.226.173.246:8080/api/server/status";
    private String urlCoins = "http://91.226.173.246:8080/api/coin/getAll";

    public TradingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        client = new OkHttpClient();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trading, container, false);

        statusServerOne = rootView.findViewById(R.id.server_status);
        layoutServerOne = rootView.findViewById(R.id.l_server_1);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        coinList = new ArrayList<>();
        coinAdapter = new CoinAdapter(coinList, new CoinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Coin coin) {
                // Переход на новый layout
                Intent intent = new Intent(getActivity(), CoinDetailActivity.class);
                intent.putExtra("coin", coin);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(coinAdapter);

        startCheckingServerStatus();
        fetchCoins(); // Получаем список монет

        return rootView;
    }

    private void startCheckingServerStatus() {
        checkServerStatus(); // Выполняем проверку статуса
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded() && getContext() != null) {
                    startCheckingServerStatus(); // Повторяем проверку каждую секунду
                }
            }
        }, 1000); // Задержка в 1 секунду
    }

    private void checkServerStatus() {
        Request request = new Request.Builder()
                .url(urlStatus)
                .addHeader("Authorization", "Bearer " + getToken()) // Добавление заголовка Authorization
                .build();

        //System.out.println("Выполняем запрос: " + request);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("Запрос не удался: " + e.getMessage());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded() && getContext() != null) {
                            //System.out.println("ON FAILURE");
                            statusServerOne.setText("Сервер недоступен!");
                            layoutServerOne.setBackground(getResources().getDrawable(R.drawable.rounded_red));
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //System.out.println("Запрос успешен: " + response);
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful() && responseBody != null) {
                        final String responseText = responseBody.string();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isAdded() && getContext() != null) {
                                    if (responseText.trim().equals("1")) {
                                        System.out.println("УСЛОВИЕ ДОСТУПНОСТИ");
                                        statusServerOne.setText("Работает");
                                        layoutServerOne.setBackground(getResources().getDrawable(R.drawable.rounded_green));
                                    } else {
                                        System.out.println("УСЛОВИЕ НЕДОСТУПНОСТИ");
                                        statusServerOne.setText("Недоступен");
                                        layoutServerOne.setBackground(getResources().getDrawable(R.drawable.rounded_red));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void fetchCoins() {
        Request request = new Request.Builder()
                .url(urlCoins)
                .addHeader("Authorization", "Bearer " + getToken()) // Добавление заголовка Authorization
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TradingFragment", "Request failed", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful() && responseBody != null) {
                        final String responseText = responseBody.string();
                        parseJson(responseText);
                    } else {
                        Log.e("TradingFragment", "Response not successful: " + response.code());
                    }
                }
            }
        });
    }

    private void parseJson(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            coinList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long id = jsonObject.getLong("id");
                String coinName = jsonObject.getString("coinName");
                String timeframe = jsonObject.getString("timeframe");
                long dateOfAddition = jsonObject.getLong("dateOfAddition");
                double minTradingQty = jsonObject.getDouble("minTradingQty");
                double maxTradingQty = jsonObject.getDouble("maxTradingQty");
                int minLeverage = jsonObject.getInt("minLeverage");
                int maxLeverage = jsonObject.getInt("maxLeverage");
                boolean dataCheck = jsonObject.getBoolean("dataCheck");
                boolean isCounted = jsonObject.getBoolean("isCounted");
                long startDateTimeCounted = jsonObject.getLong("startDateTimeCounted");
                long endDateTimeCounted = jsonObject.getLong("endDateTimeCounted");

                Coin coin = new Coin(id, coinName, timeframe, dateOfAddition, minTradingQty, maxTradingQty, minLeverage, maxLeverage, dataCheck, isCounted, startDateTimeCounted, endDateTimeCounted);
                coinList.add(coin);
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isAdded() && getContext() != null) {
                        coinAdapter.notifyDataSetChanged();
                    }
                }
            });

        } catch (JSONException e) {
            Log.e("TradingFragment", "JSON parsing error", e);
        }
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null); // Останавливаем все запланированные задачи
    }
}