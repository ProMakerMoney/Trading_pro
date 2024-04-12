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
        Double totalProfit = null;

        for(OrderPro orderPro : orders){
            totalProfit = totalProfit + orderPro.calcProfit(margin, leverage, commission);
        }

        return totalProfit;
    }

    public Double getTotalMargin(double margin){
        Double totalMargin = null;

        for(int i = 0; i < orders.size(); i++){
            totalMargin = totalMargin + margin;
        }

        return totalMargin;
    }

    public Integer getId() {
        return id;
    }

    public TYPE getType() {
        return type;
    }
}
