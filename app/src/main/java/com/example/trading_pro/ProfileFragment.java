package com.example.trading_pro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Проверяем, прикреплен ли фрагмент к активности перед использованием активности
        if (isAdded()) {
            Activity activity = getActivity();
            if (activity != null) {
                // Изменяем цвет статус-бара на активности
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.md_theme_light_shadow));

                // Найдите кнопку "Выйти" и установите для нее обработчик нажатия
                AppCompatButton logoutButton = view.findViewById(R.id.logout);
                logoutButton.setOnClickListener(v -> {
                    // Очистите сохраненные данные пользователя
                    SharedPreferences sharedPreferences = activity.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();  // Очистить все данные
                    editor.apply();

                    // Вернуть пользователя на экран входа
                    Intent intent = new Intent(activity, LoginActivityPRO.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    activity.finish();  // Завершить текущую активность
                });
            }
        }
    }
}