package com.example.trading_pro.order;


import java.time.LocalDateTime;

public class OrderCreator {


    public static Order createOrder(double entryPrice, double exitPrice, double equity, double commission, int LEVERAGE, LocalDateTime openedTime, LocalDateTime closedTime, TYPE type) {
        if(type == TYPE.LONG){
            double profit = calculateProfitLONG(entryPrice, exitPrice, equity, LEVERAGE, commission);
            return new Order(profit, equity, openedTime, closedTime, type);
        }else {
            double profit = calculateProfitSHORT(entryPrice, exitPrice, equity, LEVERAGE, commission);
            return new Order(profit, equity, openedTime, closedTime, type);
        }
    }

    public static double calculateProfitLONG(double entryPrice, double exitPrice, double margin, int leverage, double commission) {
        double tradeSize = margin * leverage; // Размер позиции с учетом плеча
        double commissionEntry = tradeSize * commission;
        double pnl; // Прибыль/убыток

        double x = exitPrice * 100 / entryPrice - 100;
        pnl = tradeSize * (1 + x/100) ;
        double commisionExit = pnl * commission;

        return pnl - tradeSize - commissionEntry - commisionExit;
    }


    public static double calculateProfitSHORT(double entryPrice, double exitPrice, double margin, int leverage, double commission) {
        double tradeSize = margin * leverage; // Размер позиции с учетом плеча
        double commissionEntry = tradeSize * commission;
        double pnl; // Прибыль/убыток

        double x = exitPrice * 100 / entryPrice - 100;
        pnl = tradeSize * (1 - x/100) ;
        double commisionExit = pnl * commission;

        return pnl - tradeSize - commissionEntry - commisionExit;
    }
}