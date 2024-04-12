package com.example.trading_pro;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.example.trading_pro.order.STATUS;
import com.example.trading_pro.order.TYPE;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TradingFragment extends Fragment {
    private TextView textView;
    private TextView statusServerOne;
    private TextView price2;
    private TextView orderNum;
    private ScheduledExecutorService executorService;
    private LinearLayout layoutServerOne;

    private Dialog dialog;

    private ImageButton lolButton;


    private double lastPrice = 0.0;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TradingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trading, container, false);
        textView = rootView.findViewById(R.id.price);
        price2 = rootView.findViewById(R.id.price2);
        orderNum = rootView.findViewById(R.id.numOrder);
        lolButton = rootView.findViewById(R.id.lolButton);

        statusServerOne = rootView.findViewById(R.id.server_status);
        layoutServerOne = rootView.findViewById(R.id.l_server_1);

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::fetchPrice, 0, 2, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(this::serverStatus, 0, 7, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(this::orderStatus, 0, 5, TimeUnit.SECONDS);

        lolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return rootView;
    }

    private void orderStatus(){
        CollectionReference ordersLogCollection = db.collection("servers")
                .document("server_one")
                .collection("ordersLog");

        ordersLogCollection.orderBy("time", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot documents = task.getResult();
                            if (!documents.isEmpty()) {
                                DocumentSnapshot lastDocument = documents.getDocuments().get(0);
                                Integer orderId = Integer.parseInt(lastDocument.getString("OrderId"));
                                Integer id = Integer.parseInt(lastDocument.getString("id"));
                                TYPE type = TYPE.valueOf(lastDocument.getString("type"));
                                double price = Double.parseDouble(lastDocument.getString("price"));
                                String formattedDateTime = lastDocument.getString("time");
                                STATUS status = STATUS.valueOf(lastDocument.getString("status"));

                                // Создаем объект OrderLog с полученными данными
                                OrderLog lastOrderLog = new OrderLog(orderId, id, type, price, parseDateTime(formattedDateTime), status);

                                // Используем lastOrderLog
                                double pri = lastOrderLog.getPrice();
                                String priceString = Double.toString(pri);
                                price2.setText(priceString);
                                orderNum.setText(lastOrderLog.getId().toString() + "/25");
                            }
                        } else {
                            // Обработка ошибки
                        }
                    }
                });
    }

    private void serverStatus() {
        CollectionReference statusCollection = db.collection("servers")
                .document("server_one")
                .collection("status");


        statusCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot documents = task.getResult();
                            LocalDateTime maxDateTime = null;
                            for (DocumentSnapshot document : documents) {
                                String dateTimeString = document.getString("Status");
                                LocalDateTime dateTime = parseDateTime(dateTimeString);
                                if (maxDateTime == null || dateTime.isAfter(maxDateTime)) {
                                    maxDateTime = dateTime;
                                }
                            }
                            System.out.println(maxDateTime);
                            if (maxDateTime != null) {
                                // Используйте полученное значение maxDateTime
                                LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("UTC"));
                                // Вычисляем разницу между текущим временем и максимальным временем из документов
                                Duration duration = Duration.between(maxDateTime, currentDateTime);

                                // Проверяем, если разница больше 2 минут
                                if (duration.toMinutes() > 1) {
                                    // Выводим разницу
                                    statusServerOne.setText("Отключен " + duration.toMinutes() + " м.");
                                    layoutServerOne.setBackground(getResources().getDrawable(R.drawable.rounded_red));

                                } else {
                                    statusServerOne.setText("Работает");
                                    layoutServerOne.setBackground(getResources().getDrawable(R.drawable.rounded_green));
                                }
                            } else {
                                // Коллекция пустая
                            }
                        } else {
                            // Ошибка получения документов
                        }
                    }
                });
    }

    private void fetchPrice() {
        var client = BybitApiClientFactory.newInstance().newAsyncMarketDataRestClient();
        var tickerRequest = MarketDataRequest.builder().category(CategoryType.LINEAR).symbol("SOLUSDT").build();
        //client.getMarketTickers(tickerRequest, System.out::println);
        client.getMarketTickers(tickerRequest, response -> {
            //System.out.println(response);
            String priceString = extractLastPrice(response.toString());
            double currentPrice = Double.parseDouble(priceString);

            if (currentPrice > lastPrice) {
                // Цена увеличилась, подсветить зеленым
                highlightTextView(textView, Color.GREEN);
            } else if (currentPrice < lastPrice) {
                // Цена уменьшилась, подсветить красным
                highlightTextView(textView, Color.RED);
            }

            textView.setText(priceString);
            lastPrice = currentPrice;
        });
    }

    public static String extractLastPrice(String input) {
        String regex = "lastPrice=([\\d.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1); // Возвращаем первую найденную группу, где находится значение
        } else {
            return "Not Found";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executorService.shutdown();
    }

    private void highlightTextView(final TextView textView, final int color) {
        textView.setTextColor(color);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Сбросить цвет текста после 500 миллисекунд
                textView.setTextColor(Color.WHITE);
            }
        }, 500);
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }


    private void showDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout, null);
        dialog = new Dialog(getContext());
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        AppCompatButton okButton = dialogView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}