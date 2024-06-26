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
import android.widget.FrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private LineChart lineChart;

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

        // Инициализация графика
        lineChart = new LineChart(getContext());
        FrameLayout chartContainer = rootView.findViewById(R.id.chartContainer);
        chartContainer.addView(lineChart);

        // Создание данных для графика
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 100));
        entries.add(new Entry(1, 180));
        entries.add(new Entry(2, 420));
        entries.add(new Entry(3, 535));

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(R.color.status_bar_green); // Установка цвета линии
        dataSet.setLineWidth(2f); // Установка толщины линии
        dataSet.setFillColor(R.color.status_bar_green); // Установка цвета заливки
        dataSet.setDrawFilled(true); // Включение заливки под графиком

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);


        // Настройка графика
        lineChart.getDescription().setEnabled(false); // Отключить описание графика
        lineChart.getLegend().setEnabled(false); // Отключить легенду

        // Настройка оси X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false); // Отключить ось X

        // Настройка оси Y
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // Отключить сетку по левой оси Y
        leftAxis.setGranularity(50f); // Шаг оси Y

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false); // Отключить правую ось Y

        lineChart.invalidate(); // refresh

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