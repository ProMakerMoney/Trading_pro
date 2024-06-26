package com.example.trading_pro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class StrategyActivity extends Activity {

    private List<Result> resultList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.md_theme_light_shadow));
        // Получаем Intent, который запустил эту активити
        Intent intent = getIntent();

        // Извлекаем переданный документ из Intent
        String documentId = intent.getStringExtra("documentId");


    }
}
