package com.example.trading_pro;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StrategyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView textView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore


    public StrategyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_strategy, container, false);
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db(view);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {
        // Здесь вы можете выполнить код для обновления данных
        // Например, загрузить новые данные из сети или базы данных
        db(getView());
        // После завершения обновления, вызовите swipeRefreshLayout.setRefreshing(false)
        swipeRefreshLayout.setRefreshing(false);
    }

    public void db (View view){
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            List<StrategyDocument> userData = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userData.add(document.toObject(StrategyDocument.class));
                            }

                            RecyclerView recyclerView = view.findViewById(R.id.strategy_info);
                            StrategyDocumentAdapter adapter = new StrategyDocumentAdapter(userData);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                            // Добавляем обработчик нажатий на элементы списка
                            adapter.setOnItemClickListener(new StrategyDocumentAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(StrategyDocument document) {
                                    // Здесь вы можете реализовать переход на новое окно, связанное с выбранным документом
                                    String documentId = document.getName(); // Получаем идентификатор документа
                                    // Добавляем Toast для отображения сообщения
                                    Toast.makeText(getActivity(), "Выбран документ: " + documentId, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), StrategyActivity.class);
                                    intent.putExtra("documentId", documentId);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}