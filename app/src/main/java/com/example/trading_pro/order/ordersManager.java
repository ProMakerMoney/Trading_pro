package com.example.trading_pro.order;

import java.util.ArrayList;
import java.util.List;

public class ordersManager {
    PrimeOrder primeOrder;
    private double DEPOSIT;
    private Integer RISK;
    private Integer LEVERAGE;

    public ordersManager(PrimeOrder primeOrder, double DEPOSIT, Integer RISK, Integer LEVERAGE) {
        this.primeOrder = primeOrder;
        this.DEPOSIT = DEPOSIT;
        this.RISK = RISK;
        this.LEVERAGE = LEVERAGE;
    }

    public PrimeOrderDocument getPrimeOrderDocument(){
        PrimeOrderDocument primeOrderDocument = new PrimeOrderDocument();
        primeOrderDocument.setId(primeOrder.getId());
        primeOrderDocument.setType(primeOrder.getType());

        return primeOrderDocument;
    }
}
