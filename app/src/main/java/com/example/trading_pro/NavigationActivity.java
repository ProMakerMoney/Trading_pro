package com.example.trading_pro;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.trading_pro.databinding.ActivityNavigationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        // Устанавливаем выбранный пункт меню
//        bottomNavigationView.setSelectedItemId(R.id.profile);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ProfileFragment());
        binding.bottomNavigationView.setSelectedItemId(R.id.profile);
        //int item1 = R.id.feed;


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.feed) {
                replaceFragment(new FeedFragment());
            } else if (id == R.id.strategy) {
                replaceFragment(new StrategyFragment());
            } else if (id == R.id.trading) {
                replaceFragment(new TradingFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
//            switch (item.getItemId()){
//                case R.id.feed:
//                    replaceFragment(new FeedFragment());
//                    break;
//                case R.id.strategy:
//                    replaceFragment(new StrategyFragment());
//                    break;
//                case R.id.trading:
//                    replaceFragment(new TradingFragment());
//                    break;
//                case R.id.profile:
//                    replaceFragment(new ProfileFragment());
//                    break;
//            }
//            return true;
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
