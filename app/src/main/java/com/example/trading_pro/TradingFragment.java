package com.example.trading_pro;


import static android.content.ContentValues.TAG;

import static com.google.firebase.firestore.DocumentChange.Type.MODIFIED;
import static com.google.firebase.firestore.DocumentChange.Type.REMOVED;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.example.trading_pro.order.OrderPro;
import com.example.trading_pro.order.OrdersManager;
import com.example.trading_pro.order.PrimeOrder;
import com.example.trading_pro.order.PrimeOrderDocument;
import com.example.trading_pro.order.PrimeOrderDocumentAdapter;
import com.example.trading_pro.order.STATUS;
import com.example.trading_pro.order.TYPE;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TradingFragment extends Fragment {

    private Double DEPOSIT = 100.0;
    private Integer RISK = 10;
    private Integer LEVERAGE = 25;

    private Double FINAL_DEPOSIT = DEPOSIT;
    private Double TOTAL_PROFIT = (double) 0;
    private TextView textView;
    private TextView statusServerOne;
    private TextView price2;
    private TextView orderNum;
    private ScheduledExecutorService executorService;
    private LinearLayout layoutServerOne;

    private TextView settings;

    private Dialog dialog;

    private ImageButton lolButton;

    RecyclerView recyclerView;

    TextView finalDeposit;
    TextView totalProfit;
    TextView current_price_per;

    TextView type_order;

    TextView margin;
    TextView current_profit;

    LinearLayout order_color;

    private ListenerRegistration statusListenerRegistration;
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
        price2 = rootView.findViewById(R.id.current_price);
        orderNum = rootView.findViewById(R.id.numOrder);
        lolButton = rootView.findViewById(R.id.lolButton);

        statusServerOne = rootView.findViewById(R.id.server_status);
        layoutServerOne = rootView.findViewById(R.id.l_server_1);

        finalDeposit = rootView.findViewById(R.id.final_deposit);
        totalProfit = rootView.findViewById(R.id.totalProfit);

        current_price_per = rootView.findViewById(R.id.current_per);
        type_order = rootView.findViewById(R.id.type_order);
        margin = rootView.findViewById(R.id.margin);
        current_profit = rootView.findViewById(R.id.current_profit);
        order_color = rootView.findViewById(R.id.order_color);


        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::fetchPrice, 0, 2, TimeUnit.SECONDS);
        //executorService.scheduleAtFixedRate(this::serverStatus, 0, 10, TimeUnit.SECONDS);

        getPrimeOrders();

        // Вместо выполнения запроса каждые несколько секунд добавляем SnapshotListener
        setupStatusListener();

        recyclerView = rootView.findViewById(R.id.trading_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //executorService.scheduleAtFixedRate(this::ordersList, 0, 1, TimeUnit.MINUTES);



        lolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return rootView;
    }

    public void getPrimeOrders(){
        CollectionReference primeOrdersRef = db.collection("servers")
                .document("server_one")
                .collection("primeOrders");

        primeOrdersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Обработка ошибки
                    return;
                }

                List<PrimeOrder> primeOrderList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Обработка полученных данных как раньше
                    // Извлечение данных PrimeOrder
                    int id = documentSnapshot.getLong("ID").intValue();
                    String typeString = documentSnapshot.getString("Type");
                    TYPE type = TYPE.valueOf(typeString);

                    // Извлечение данных Orders
                    Map<String, Object> ordersMap = (Map<String, Object>) documentSnapshot.get("Orders");
                    List<OrderPro> orders = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : ordersMap.entrySet()) {
                        Map<String, Object> orderMap = (Map<String, Object>) entry.getValue();
                        OrderPro order = new OrderPro(
                                Integer.parseInt(orderMap.get("id").toString()),
                                Double.parseDouble(orderMap.get("enterPrice").toString()),
                                Double.parseDouble(orderMap.get("exitPrice").toString()),
                                LocalDateTime.parse(orderMap.get("enterTime").toString()),
                                LocalDateTime.parse(orderMap.get("exitTime").toString()),
                                TYPE.valueOf(orderMap.get("type").toString())
                        );
                        orders.add(order);
                    }
                    // Создание объекта PrimeOrder и добавление в список
                    PrimeOrder primeOrder = new PrimeOrder(id, orders, type);
                    primeOrderList.add(primeOrder);

                }
                Collections.reverse(primeOrderList);
                if (getActivity() != null) {
                    // Обновление UI
                    getActivity().runOnUiThread(() -> {
                        updateUI(primeOrderList);
                    });
                }
            }
        });
    }

    private void updateUI(List<PrimeOrder> primeOrderList) {
        OrdersManager ordersManager = new OrdersManager(primeOrderList, DEPOSIT, RISK, LEVERAGE);
        // Дальнейшие действия по обновлению UI


        List<PrimeOrderDocument> primeOrderDocumentList = ordersManager.getPrimeOrderDocument();
        PrimeOrderDocumentAdapter adapter = new PrimeOrderDocumentAdapter(primeOrderDocumentList);
        recyclerView.setAdapter(adapter);

        TOTAL_PROFIT = ordersManager.getTotalProfit();
        FINAL_DEPOSIT = ordersManager.getDEPOSIT();
        totalProfit.setText(String.format("%.2f",TOTAL_PROFIT));
        finalDeposit.setText(String.format("%.2f",FINAL_DEPOSIT));

    }

//    public List<PrimeOrder> getPrimeOrders(View view){
//        CollectionReference primeOrdersRef = db.collection("servers")
//                .document("server_one")
//                .collection("primeOrders");
//
//        List<PrimeOrder> primeOrderList = new ArrayList<>();
//
//        primeOrdersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    // Обработка ошибки
//                    return;
//                }
//
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    // Извлечение данных PrimeOrder
//                    int id = documentSnapshot.getLong("ID").intValue();
//                    String typeString = documentSnapshot.getString("Type");
//                    TYPE type = TYPE.valueOf(typeString);
//
//                    // Извлечение данных Orders
//                    Map<String, Object> ordersMap = (Map<String, Object>) documentSnapshot.get("Orders");
//                    List<OrderPro> orders = new ArrayList<>();
//                    for (Map.Entry<String, Object> entry : ordersMap.entrySet()) {
//                        Map<String, Object> orderMap = (Map<String, Object>) entry.getValue();
//                        OrderPro order = new OrderPro(
//                                Integer.parseInt(orderMap.get("id").toString()),
//                                Double.parseDouble(orderMap.get("enterPrice").toString()),
//                                Double.parseDouble(orderMap.get("exitPrice").toString()),
//                                LocalDateTime.parse(orderMap.get("enterTime").toString()),
//                                LocalDateTime.parse(orderMap.get("exitTime").toString()),
//                                TYPE.valueOf(orderMap.get("type").toString())
//                        );
//                        orders.add(order);
//                    }
//
//                    // Создание объекта PrimeOrder и добавление в список
//                    PrimeOrder primeOrder = new PrimeOrder(id, orders, type);
//                    primeOrderList.add(primeOrder);
//                }
//                Collections.reverse(primeOrderList);
//
//
//
//                // Дальнейшая обработка списка primeOrderList
//                OrdersManager ordersManager = new OrdersManager(primeOrderList, DEPOSIT, RISK, LEVERAGE);
//                ordersManager.getPrimeOrderDocument();
//                // Предполагаем, что последняя сделка в списке - новая сделка
//
//
//                TOTAL_PROFIT = ordersManager.getTotalProfit();
//                FINAL_DEPOSIT = ordersManager.getDEPOSIT();
//                totalProfit.setText(String.format("%.2f",TOTAL_PROFIT));
//                finalDeposit.setText(String.format("%.2f",FINAL_DEPOSIT));
//
//                List<PrimeOrderDocument> primeOrderDocumentList = ordersManager.getPrimeOrderDocument();
//                PrimeOrderDocumentAdapter adapter = new PrimeOrderDocumentAdapter(primeOrderDocumentList);
//                recyclerView.setAdapter(adapter);
//            }
//        });
//
//        return primeOrderList;
//    }

    private void getInfoDb(){
        DocumentReference documentReference = db.collection("servers")
                .document("server_one");
        CollectionReference ordersLogCollection = documentReference.collection("ordersLog");


    }

    private void orderStatus(double currentPrice){
        CollectionReference ordersLogCollection = db.collection("servers")
                .document("server_one")
                .collection("ordersLog");

        ordersLogCollection.orderBy("time", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Обработка ошибки
                            return;
                        }

                        if (snapshots != null && !snapshots.isEmpty()) {
                            DocumentChange lastChange = snapshots.getDocumentChanges().get(0);
                            DocumentSnapshot lastDocument = lastChange.getDocument();

                            if (lastChange.getType() == DocumentChange.Type.ADDED) {
                                // Это новая запись, обновляем TextView

                                System.out.println("ПРОВЕРКА");

                                Integer orderId = Integer.parseInt(lastDocument.getString("OrderId"));
                                Integer id = Integer.parseInt(lastDocument.getString("id"));
                                TYPE type = TYPE.valueOf(lastDocument.getString("type"));
                                double price = Double.parseDouble(lastDocument.getString("price"));
                                String formattedDateTime = lastDocument.getString("time");
                                STATUS status = STATUS.valueOf(lastDocument.getString("status"));
                                double avg = Double.parseDouble(lastDocument.getString("avg"));

                                // Создаем объект OrderLog с полученными данными
                                OrderLog lastOrderLog = new OrderLog(orderId, id, type, price, parseDateTime(formattedDateTime), status, avg);

                                // Используем lastOrderLog
                                double pri = lastOrderLog.getAvg();
                                price2.setText(String.format("%.2f", pri));
                                orderNum.setText(lastOrderLog.getId().toString() + "/25");
                                type_order.setText(type.toString());
                                double mar = id * (FINAL_DEPOSIT*RISK/100) / 25;
                                margin.setText(String.format("%.2f", mar) + "$");


                                if(type == TYPE.LONG){
                                    double current_per = (currentPrice * 100 / pri  - 100) * LEVERAGE;
                                    current_price_per.setText(String.format("%.2f", current_per) + "%");
                                    double prof = mar * current_per/100;
                                    current_profit.setText((String.format("%.2f", prof) + "$"));
                                    if(current_per < 0){
                                        order_color.setBackground(getResources().getDrawable(R.drawable.rounded_red_pro));
                                    }else {
                                        order_color.setBackground(getResources().getDrawable(R.drawable.rounded_green_pro));
                                    }
                                }else {
                                    double current_per = (100 - currentPrice * 100 / pri) * LEVERAGE;
                                    current_price_per.setText(String.format("%.2f", current_per) + "%");
                                    double prof = mar * current_per/100;
                                    current_profit.setText((String.format("%.2f", prof) + "$"));
                                    if(current_per < 0){
                                        order_color.setBackground(getResources().getDrawable(R.drawable.rounded_red_pro));
                                    }else {
                                        order_color.setBackground(getResources().getDrawable(R.drawable.rounded_green_pro));
                                    }
                                }

                            }
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

            orderStatus(currentPrice);
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

        settings = dialogView.findViewById(R.id.settings);
        settings.setText("Деп: " + DEPOSIT + " || Риск: " + RISK + " || Плечо: " + LEVERAGE);

        AppCompatButton okButton = dialogView.findViewById(R.id.okButton);
        EditText deposit = dialogView.findViewById(R.id.deposit);
        EditText risk = dialogView.findViewById(R.id.risk);
        EditText leverage = dialogView.findViewById(R.id.leverage);

        deposit.setText(DEPOSIT.toString());
        risk.setText(RISK.toString());
        leverage.setText(LEVERAGE.toString());
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String depositText = deposit.getText().toString();
                String riskText = risk.getText().toString();
                String leverageText = leverage.getText().toString();

                // Проверка, что поля не пустые
                if (!depositText.isEmpty() && !riskText.isEmpty() && !leverageText.isEmpty()) {
                    try {
                        DEPOSIT = Double.parseDouble(depositText);
                        RISK = Integer.parseInt(riskText);
                        LEVERAGE = 25;
                        // Дальнейшие действия с данными
                        settings.setText("Деп: " + DEPOSIT + " || Риск: " + RISK + " || Плечо: " + LEVERAGE);

                        getPrimeOrders(); ///!!!

                    } catch (NumberFormatException e) {
                        // Обработка ошибки, если введенные значения некорректны
                        e.printStackTrace();
                    }
                } else {
                    // Обработка ошибки, если одно или несколько полей пустые
                }

                dialog.dismiss();
            }
        });
    }



    private void sendNotification(String notificationDetails) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "trading_notifications";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Trading Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), channelId)
                .setSmallIcon(R.drawable.baseline_strategy_24) // Установите свой идентификатор ресурса иконки уведомления
                .setContentTitle("Pin Bot")
                .setContentText(notificationDetails)
                .setAutoCancel(true);

        notificationManager.notify(new Random().nextInt(), builder.build());
    }


    private void setupStatusListener() {
        CollectionReference statusRef = db.collection("servers").document("server_one").collection("status");

        statusListenerRegistration = statusRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Обработка возможной ошибки
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                // Обработка добавленных, измененных или удаленных документов внутри коллекции
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    DocumentSnapshot snapshot = dc.getDocument();
                    switch (dc.getType()) {
                        case ADDED: {
                            // Обрабатываем добавленный статус
                            Log.d(TAG, "New status: " + snapshot.getData());


                            LocalDateTime maxDateTime = null;
                            String dateTimeString = snapshot.getString("Status");
                            LocalDateTime dateTime = parseDateTime(dateTimeString);
                            if (maxDateTime == null || dateTime.isAfter(maxDateTime)) {
                                maxDateTime = dateTime;
                            }

                            System.out.println(maxDateTime);
                            if (maxDateTime != null) {
                                // Используйте полученное значение maxDateTime
                                LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("UTC"));
                                // Вычисляем разницу между текущим временем и максимальным временем из документов
                                Duration duration = Duration.between(maxDateTime, currentDateTime);

                                // Проверяем, если разница больше 2 минут
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    if (duration.toSeconds() > 40) {
                                        // Выводим разницу
                                        statusServerOne.setText("Отключен " + duration.toMinutes() + " м.");
                                        layoutServerOne.setBackground(getResources().getDrawable(R.drawable.rounded_red));

                                    } else {
                                        statusServerOne.setText("Работает");
                                        //sendNotification("Сервер включился в " + LocalDateTime.now().toLocalTime());
                                        layoutServerOne.setBackground(getResources().getDrawable(R.drawable.rounded_green));
                                    }
                                }
                                break;
                            }
                        }
                            case MODIFIED:
                                // Обрабатываем измененный статус
                                Log.d(TAG, "Modified status: " + snapshot.getData());
                                break;
                            case REMOVED:
                                // Обрабатываем удаленный статус
                                Log.d(TAG, "Removed status: " + snapshot.getData());
                                break;
                        }
                        // Дополнительно можно обновить UI в соответствии с изменениями

                    }
                }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (statusListenerRegistration != null) {
            statusListenerRegistration.remove(); // Отписываемся от слушателя
        }
    }

}