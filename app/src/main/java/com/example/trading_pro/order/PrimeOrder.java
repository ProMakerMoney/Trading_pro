package com.example.trading_pro.order;

import java.util.List;

public class PrimeOrder {
    double DEPOSIT;
    double RISK;
    double LEVERAGE;

    List<Order> orderList;

    Integer id;
    TYPE type;
    Integer numOrders;
    double perProfit;
    double profit;
    double perOfDeposit;

    public PrimeOrder(double DEPOSIT, double RISK, double LEVERAGE, List<Order> orderList) {
        this.DEPOSIT = DEPOSIT;
        this.RISK = RISK;
        this.LEVERAGE = LEVERAGE;
        this.orderList = orderList;
    }
}
