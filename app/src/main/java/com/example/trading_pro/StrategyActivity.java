package com.example.trading_pro;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StrategyActivity extends Activity {

    private List<Result> resultList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_item);
        getWindow().setStatusBarColor(getResources().getColor(R.color.md_theme_light_shadow));
        // Получаем Intent, который запустил эту активити
        Intent intent = getIntent();

        // Извлекаем переданный документ из Intent
        String documentId = intent.getStringExtra("documentId");
        TextView itemName = findViewById(R.id.strategyName);
        itemName.setText(documentId);


        // Добавляем 5 новых элементов в список
        resultList.add(new Result()); // Добавьте необходимые параметры в конструктор
        resultList.add(new Result());
        resultList.add(new Result());
        resultList.add(new Result());
        resultList.add(new Result());

        System.out.println("*** - " + resultList.size());

        RecyclerView recyclerView = findViewById(R.id.strategy_res);
        ResultAdapter adapter = new ResultAdapter(resultList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
