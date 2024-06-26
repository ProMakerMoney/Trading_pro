package com.example.trading_pro.coin;


import java.io.Serializable;

public class Coin implements Serializable {

    private Long id;
    private String coinName;
    private String timeframe;
    private long dateOfAddition;
    private double minTradingQty;
    private double maxTradingQty;
    private int minLeverage;
    private int maxLeverage;
    private boolean dataCheck;
    private boolean isCounted;
    private long startDateTimeCounted;
    private long endDateTimeCounted;

    public Coin(Long id, String coinName, String timeframe, long dateOfAddition, double minTradingQty, double maxTradingQty, int minLeverage, int maxLeverage, boolean dataCheck, boolean isCounted, long startDateTimeCounted, long endDateTimeCounted) {
        this.id = id;
        this.coinName = coinName;
        this.timeframe = timeframe;
        this.dateOfAddition = dateOfAddition;
        this.minTradingQty = minTradingQty;
        this.maxTradingQty = maxTradingQty;
        this.minLeverage = minLeverage;
        this.maxLeverage = maxLeverage;
        this.dataCheck = dataCheck;
        this.isCounted = isCounted;
        this.startDateTimeCounted = startDateTimeCounted;
        this.endDateTimeCounted = endDateTimeCounted;
    }


    public void setIsCounted(boolean isCounted) {
        this.isCounted = isCounted;
    }

    public Object getDataCheck() {
        return dataCheck;
    }

    public Object getIsCounted() {
        return isCounted;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public Boolean isCounted() {
        return isCounted;
    }
}