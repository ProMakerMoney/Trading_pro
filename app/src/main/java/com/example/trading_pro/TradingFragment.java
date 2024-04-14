package com.example.trading_pro;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private Integer LEVERAGE = 27;

    private Double FINAL_DEPOSIT = DEPOSIT;
    private Double TOTAL_PROFIT = (double) 0;
    private TextView textView;
    private TextView statusServerOne;
    private TextView price2;
    private TextView orderNum;
    private ScheduledExecutorService executorService;
    private LinearLayout layoutServerOne;

    private TextView marqueeTextView;
    private TextView settings;

    private Dialog dialog;

    private ImageButton lolButton;

    RecyclerView recyclerView;

    TextView finalDeposit;
    TextView totalProfit;


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

        finalDeposit = rootView.findViewById(R.id.final_deposit);
        totalProfit = rootView.findViewById(R.id.totalProfit);

        marqueeTextView = rootView.findViewById(R.id.marqueeTextView);
        marqueeTextView.setSelected(true);// Начинает анимацию бегущей полосы

        settings = rootView.findViewById(R.id.settings);
        settings.setText("Деп: " + DEPOSIT + " || Риск: " + RISK + " || Плечо: " + LEVERAGE);

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::fetchPrice, 0, 2, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(this::serverStatus, 0, 7, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(this::orderStatus, 0, 5, TimeUnit.SECONDS);

        recyclerView = rootView.findViewById(R.id.trading_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        executorService.scheduleAtFixedRate(this::ordersList, 0, 1, TimeUnit.MINUTES);



        lolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return rootView;
    }

    private void ordersList(){
        getPrimeOrders(recyclerView);
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
                                orderNum.setText(lastOrderLog.getId().toString() + "/17");

                                marqueeTextView.setText(lastOrderLog.getId().toString() + "/17  ||  " + type.toString() + "  ||  " + priceString);
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
                        LEVERAGE = 27;
                        // Дальнейшие действия с данными
                        settings.setText("Деп: " + DEPOSIT + " || Риск: " + RISK + " || Плечо: " + LEVERAGE);

                        getPrimeOrders(recyclerView); ///!!!

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

    public List<PrimeOrder> getPrimeOrders(View view){
        CollectionReference primeOrdersRef = db.collection("servers")
                .document("server_one")
                .collection("primeOrders");

        List<PrimeOrder> primeOrderList = new ArrayList<>();

        primeOrdersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Обработка ошибки
                    return;
                }

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
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



                // Дальнейшая обработка списка primeOrderList
                OrdersManager ordersManager = new OrdersManager(primeOrderList, DEPOSIT, RISK, LEVERAGE);
                ordersManager.getPrimeOrderDocument();
                // Предполагаем, что последняя сделка в списке - новая сделка


                TOTAL_PROFIT = ordersManager.getTotalProfit();
                FINAL_DEPOSIT = ordersManager.getDEPOSIT();
                totalProfit.setText(String.format("%.2f",TOTAL_PROFIT));
                finalDeposit.setText(String.format("%.2f",FINAL_DEPOSIT));

                List<PrimeOrderDocument> primeOrderDocumentList = ordersManager.getPrimeOrderDocument();
                PrimeOrderDocumentAdapter adapter = new PrimeOrderDocumentAdapter(primeOrderDocumentList);
                recyclerView.setAdapter(adapter);
            }
        });

        return primeOrderList;
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
                .setSmallIcon(R.drawable.profile_image_2) // Установите свой идентификатор ресурса иконки уведомления
                .setContentTitle("Новая сделка")
                .setContentText(notificationDetails)
                .setAutoCancel(true);

        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}