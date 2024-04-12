package com.example.trading_pro.order;

import java.time.LocalDateTime;


public class Order {
    private double profit;
    private double margin;
    private LocalDateTime openedTime;
    private LocalDateTime closedTime;
    private TYPE type;

    public Order(double profit, double margin, LocalDateTime openedTime, LocalDateTime closedTime, TYPE type) {
        this.profit = profit;
        this.margin = margin;
        this.openedTime = openedTime;
        this.closedTime = closedTime;
        this.type = type;
    }

    public double getProfit() {
        return profit;
    }

    public double getMargin() {
        return margin;
    }

    public LocalDateTime getOpenedTime() {
        return openedTime;
    }

    public LocalDateTime getClosedTime() {
        return closedTime;
    }

    public TYPE getType() {
        return type;
    }
}