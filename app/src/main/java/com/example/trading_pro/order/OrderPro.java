package com.example.trading_pro.order;


import java.time.LocalDateTime;

public class OrderPro {

    private Integer id;

    private Double enterPrice;

    private Double exitPrice;

    private LocalDateTime enterTime;


    private LocalDateTime exitTime;

    private TYPE type;

    public OrderPro() {

    }

    public OrderPro(Integer id, Double enterPrice, Double exitPrice, LocalDateTime enterTime, LocalDateTime exitTime, TYPE type) {
        this.id = id;
        this.enterPrice = enterPrice;
        this.exitPrice = exitPrice;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.type = type;
    }

    public Double calcProfit(double margin, int leverage, double commission){
        Double profit;
        if(type == TYPE.LONG){
            double tradeSize = margin * leverage; // Размер позиции с учетом плеча
            double commissionEntry = tradeSize * commission;
            double pnl; // Прибыль/убыток

            double x = exitPrice * 100 / enterPrice - 100;
            pnl = tradeSize * (1 + x/100) ;
            double commisionExit = pnl * commission;
            profit = pnl - tradeSize - commissionEntry - commisionExit;
        }else {
            double tradeSize = margin * leverage; // Размер позиции с учетом плеча
            double commissionEntry = tradeSize * commission;
            double pnl; // Прибыль/убыток

            double x = exitPrice * 100 / enterPrice - 100;
            pnl = tradeSize * (1 - x/100) ;
            double commisionExit = pnl * commission;
            profit = pnl - tradeSize - commissionEntry - commisionExit;
        }
        return profit;
    }

    public Double getEnterPrice() {
        return enterPrice;
    }

    public Double getExitPrice() {
        return exitPrice;
    }
}
