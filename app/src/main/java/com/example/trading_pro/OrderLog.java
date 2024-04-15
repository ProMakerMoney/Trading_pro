package com.example.trading_pro;

import com.example.trading_pro.order.STATUS;
import com.example.trading_pro.order.TYPE;

import java.time.LocalDateTime;

public class OrderLog {
    private Integer orderId;
    private Integer id;
    private TYPE type;
    private double price;
    private LocalDateTime time;
    private STATUS status;

    private Double avg;

    public OrderLog(Integer orderId, Integer id, TYPE type, double price, LocalDateTime time, STATUS status, Double avg) {
        this.orderId = orderId;
        this.id = id;
        this.type = type;
        this.price = price;
        this.time = time;
        this.status = status;
        this.avg = avg;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getId() {
        return id;
    }

    public TYPE getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public STATUS getStatus() {
        return status;
    }
}
