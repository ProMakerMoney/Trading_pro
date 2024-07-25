package com.example.trading_pro.bybit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TradebotApiService {

    @GET("api/tradebot/closePair/{tradingPair}")
    Call<Void> closeTradingPair(@Path("tradingPair") String tradingPair);
}
