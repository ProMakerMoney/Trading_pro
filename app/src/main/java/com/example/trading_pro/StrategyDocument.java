package com.example.trading_pro;

public class StrategyDocument {
    private String name;
    private String description;

    private String status;



    public void setDescription(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }



    public StrategyDocument() {} // Пустой конструктор необходим для Firestore

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDescription(String description) {this.description = description;}
    public String getDescription() {
        return description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }





}
