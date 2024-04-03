package com.example.trading_pro;

public class StrategyDocument {
    private String name;
    private String email;

    public StrategyDocument() {} // Пустой конструктор необходим для Firestore

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
