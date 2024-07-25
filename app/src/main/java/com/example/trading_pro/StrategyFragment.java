package com.example.trading_pro;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.example.trading_pro.bybit.BybitApi;
import com.example.trading_pro.bybit.Position;
import com.example.trading_pro.bybit.PositionAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StrategyFragment extends Fragment implements PositionAdapter.OnCloseButtonClickListener {

    private RecyclerView recyclerView;
    private PositionAdapter adapter;
    private BybitApi bybitApi;
    private Gson gson;
    private Handler handler;
    private Runnable runnable;
    private OkHttpClient httpClient;

    private static final String BASE_URL = "http://91.226.173.246:8888/";
    private static final String API_ENDPOINT = "api/tradebot/closePair/";

    final static String API_KEY = "bvQRWwQU8QapNl3Ppl";
    final static String API_SECRET = "P5h8tnabkftRGzrdFV4DXbggI7XJnaaXx6KY";

    public StrategyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация BybitApi с вашими API ключами
        bybitApi = new BybitApi(); // true для тестнета
        handler = new Handler(Looper.getMainLooper());
        httpClient = new OkHttpClient();
        gson = new Gson();
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

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Инициализация адаптера
        adapter = new PositionAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Создаем и запускаем Runnable для периодического выполнения getOpenPositions
        runnable = new Runnable() {
            @Override
            public void run() {
                getOpenPositions();
                handler.postDelayed(this, 500); // Запускаем каждые 0.5 секунды
            }
        };
        handler.post(runnable); // Первый запуск
    }

    public void getOpenPositions() {
        var client = BybitApiClientFactory.newInstance(API_KEY, API_SECRET, BybitApiConfig.MAINNET_DOMAIN).newAsyncPositionRestClient();
        var positionListRequest = PositionDataRequest.builder().category(CategoryType.LINEAR).symbol(null).settleCoin("USDT").build();

        client.getPositionInfo(positionListRequest, response -> {
            System.out.println("ОТВЕТ: " + response);

            // Создание Gson объекта
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.toJsonTree(response).getAsJsonObject();
            int retCode = jsonResponse.get("retCode").getAsInt();

            if (retCode == 0) {
                JsonObject result = jsonResponse.getAsJsonObject("result");
                JsonArray positionsArray = result.getAsJsonArray("list");

                List<Position> positions = new ArrayList<>();
                for (JsonElement element : positionsArray) {
                    JsonObject positionObject = element.getAsJsonObject();
                    Position position = new Position();
                    position.setPositionIdx(getIntSafe(positionObject, "positionIdx"));
                    position.setRiskId(getIntSafe(positionObject, "riskId"));
                    position.setRiskLimitValue(getStringSafe(positionObject, "riskLimitValue"));
                    position.setSymbol(getStringSafe(positionObject, "symbol"));
                    position.setSide(getStringSafe(positionObject, "side"));
                    position.setSize(getStringSafe(positionObject, "size"));
                    position.setAvgPrice(getStringSafe(positionObject, "avgPrice"));
                    position.setPositionValue(getStringSafe(positionObject, "positionValue"));
                    position.setTradeMode(getIntSafe(positionObject, "tradeMode"));
                    position.setPositionStatus(getStringSafe(positionObject, "positionStatus"));
                    position.setAutoAddMargin(getIntSafe(positionObject, "autoAddMargin"));
                    position.setAdlRankIndicator(getIntSafe(positionObject, "adlRankIndicator"));
                    position.setLeverage(getStringSafe(positionObject, "leverage"));
                    position.setPositionBalance(getStringSafe(positionObject, "positionBalance"));
                    position.setMarkPrice(getStringSafe(positionObject, "markPrice"));
                    position.setLiqPrice(getStringSafe(positionObject, "liqPrice"));
                    position.setBustPrice(getStringSafe(positionObject, "bustPrice"));
                    position.setPositionMM(getStringSafe(positionObject, "positionMM"));
                    position.setPositionIM(getStringSafe(positionObject, "positionIM"));
                    position.setTpslMode(getStringSafe(positionObject, "tpslMode"));
                    position.setTakeProfit(getStringSafe(positionObject, "takeProfit"));
                    position.setStopLoss(getStringSafe(positionObject, "stopLoss"));
                    position.setTrailingStop(getStringSafe(positionObject, "trailingStop"));
                    position.setUnrealisedPnl(getStringSafe(positionObject, "unrealisedPnl"));
                    position.setCurRealisedPnl(getStringSafe(positionObject, "curRealisedPnl"));
                    position.setCumRealisedPnl(getStringSafe(positionObject, "cumRealisedPnl"));
                    position.setSeq(getLongSafe(positionObject, "seq"));
                    position.setReduceOnly(getBooleanSafe(positionObject, "isReduceOnly"));
                    position.setMmrSysUpdateTime(getStringSafe(positionObject, "mmrSysUpdateTime"));
                    position.setLeverageSysUpdatedTime(getStringSafe(positionObject, "leverageSysUpdatedTime"));
                    position.setSessionAvgPrice(getStringSafe(positionObject, "sessionAvgPrice"));
                    position.setCreatedTime(getLongSafe(positionObject, "createdTime"));
                    position.setUpdatedTime(getLongSafe(positionObject, "updatedTime"));

                    positions.add(position);
                }

                getActivity().runOnUiThread(() -> adapter.updatePositions(positions));
            } else {
                String errorMessage = jsonResponse.get("retMsg").getAsString();
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private String getStringSafe(JsonObject jsonObject, String memberName) {
        JsonElement element = jsonObject.get(memberName);
        return (element != null && !element.isJsonNull()) ? element.getAsString() : "";
    }

    private int getIntSafe(JsonObject jsonObject, String memberName) {
        JsonElement element = jsonObject.get(memberName);
        return (element != null && !element.isJsonNull()) ? element.getAsInt() : 0;
    }

    private long getLongSafe(JsonObject jsonObject, String memberName) {
        JsonElement element = jsonObject.get(memberName);
        return (element != null && !element.isJsonNull()) ? element.getAsLong() : 0L;
    }

    private boolean getBooleanSafe(JsonObject jsonObject, String memberName) {
        JsonElement element = jsonObject.get(memberName);
        return (element != null && !element.isJsonNull()) && element.getAsBoolean();
    }


    @Override
    public void onCloseButtonClick(String tradingPair) {
        String url = StrategyFragment.BASE_URL + StrategyFragment.API_ENDPOINT + tradingPair;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    int retCode = jsonResponse.get("retCode").getAsInt();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (retCode == 0) {
                            Toast.makeText(getContext(), "Trade closed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to close trade: " + jsonResponse.get("retMsg").getAsString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(getContext(), "Failed to close trade", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}

