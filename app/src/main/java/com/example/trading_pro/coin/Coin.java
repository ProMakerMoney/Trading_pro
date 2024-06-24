package com.example.trading_pro.coin;

import java.io.Serializable;

public class Coin implements Serializable {
    private int id;
    private String coinName;
    private String timeframe;
    private boolean isCounted;

    // Конструктор
    public Coin(int id, String coinName, String timeframe, boolean isCounted) {
        this.id = id;
        this.coinName = coinName;
        this.timeframe = timeframe;
        this.isCounted = isCounted;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public boolean isCounted() {
        return isCounted;
    }
}