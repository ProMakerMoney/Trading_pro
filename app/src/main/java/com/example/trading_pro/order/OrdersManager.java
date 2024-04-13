package com.example.trading_pro.order;

import java.util.ArrayList;
import java.util.List;

public class OrdersManager {
    List<PrimeOrder> primeOrderList;
    private Double DEPOSIT;
    private Integer RISK;
    private Integer LEVERAGE;

    private Double margin;
    private Double totalProfit = (double) 0;

    public OrdersManager(List<PrimeOrder> primeOrderList, Double DEPOSIT, Integer RISK, Integer LEVERAGE) {
        this.primeOrderList = primeOrderList;
        this.DEPOSIT = DEPOSIT;
        this.RISK = RISK;
        this.LEVERAGE = LEVERAGE;
        this.margin = (DEPOSIT * RISK/100 * LEVERAGE) / 25;
    }

    public List<PrimeOrderDocument> getPrimeOrderDocument(){

        List<PrimeOrderDocument> primeOrderDocumentList = new ArrayList<>();


        for(PrimeOrder primeOrder : primeOrderList){
            PrimeOrderDocument primeOrderDocument = new PrimeOrderDocument();
            primeOrderDocument.setId(primeOrder.getId());
            primeOrderDocument.setType(primeOrder.getType());
            primeOrderDocument.setMargin(margin * primeOrder.orders.size());
            primeOrderDocument.setPerProfitOrder(primeOrder.getPerProfitOrder() * LEVERAGE);
            primeOrderDocument.setProfit(primeOrder.getTolalProfit(margin,LEVERAGE, 0.00005));
            primeOrderDocument.setPerProfitDeposit(primeOrder.getProfitDeposit(DEPOSIT,margin,LEVERAGE,0.00005));

            primeOrderDocumentList.add(primeOrderDocument);

            DEPOSIT = DEPOSIT + primeOrder.getTolalProfit(margin,LEVERAGE, 0.00005);
            totalProfit = totalProfit + primeOrder.getTolalProfit(margin,LEVERAGE, 0.00005);
        }

        return primeOrderDocumentList;
    }

    public Double getTotalProfit() {
        return totalProfit;
    }

    public Double getDEPOSIT() {
        return DEPOSIT;
    }
}
