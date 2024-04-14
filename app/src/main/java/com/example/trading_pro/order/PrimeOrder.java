package com.example.trading_pro.order;


import java.util.List;

public class PrimeOrder {

    private Integer id;

    private TYPE type;

    List<OrderPro> orders;

    public PrimeOrder(Integer id, List<OrderPro> orders, TYPE type) {
        this.id = id;
        this.orders = orders;
        this.type = type;
    }

    public Double getTolalProfit(double margin, int leverage, double commission){
        Double totalProfit = (double) 0;

        for(OrderPro orderPro : orders){
            totalProfit = totalProfit + orderPro.calcProfit(margin, leverage, commission);
        }

        return totalProfit/leverage;
    }


    public Integer getId() {
        return id;
    }

    public TYPE getType() {
        return type;
    }

    public Double getPerProfitOrder() {

        Double enterPriceSum = (double) 0;
        for(OrderPro orderPro : orders){
            enterPriceSum = enterPriceSum + orderPro.getEnterPrice();
        }
        Double enterPriceAVG = enterPriceSum / orders.size();

        if(type == TYPE.LONG){
            return (orders.get(0).getExitPrice() * 100) / enterPriceAVG - 100;
        }else{
            return 100 - (orders.get(0).getExitPrice() * 100) / enterPriceAVG;
        }


    }

    public Double getProfitDeposit(double deposit, Double margin, Integer leverage, double v) {
        return getTolalProfit(margin, leverage, 0.00005) * 100 / (deposit * leverage);
    }
}
