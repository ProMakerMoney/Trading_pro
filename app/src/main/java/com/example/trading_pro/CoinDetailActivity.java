package com.example.trading_pro;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trading_pro.coin.Coin;

public class CoinDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_layout);

        Coin coin = (Coin) getIntent().getSerializableExtra("coin");

        TextView coinNameTextView = findViewById(R.id.item_coin_name);


        coinNameTextView.setText(coin.getCoinName());

    }
}
