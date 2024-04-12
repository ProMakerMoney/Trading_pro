package com.example.trading_pro.order;

public class PrimeOrderDocument {
    private Integer id;
    private TYPE type;
    private Double margin;
    private Double perProfitOrder;
    private Double profit;
    private Double perProfitDeposit;


    public PrimeOrderDocument(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getPerProfitOrder() {
        return perProfitOrder;
    }

    public void setPerProfitOrder(Double perProfitOrder) {
        this.perProfitOrder = perProfitOrder;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getPerProfitDeposit() {
        return perProfitDeposit;
    }

    public void setPerProfitDeposit(Double perProfitDeposit) {
        this.perProfitDeposit = perProfitDeposit;
    }
}
