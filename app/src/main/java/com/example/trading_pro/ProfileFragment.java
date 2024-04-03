package com.example.trading_pro;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


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
        // Получаем ссылку на активность, которая содержит этот фрагмент
        Activity activity = requireActivity();

        // Изменяем цвет статус-бара на активности
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.md_theme_light_shadow));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            System.out.println("UID - " + userId);

            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    System.out.println("БАРЬЕР 1");
                    if (task.isSuccessful()) {
                        System.out.println("БАРЬЕР 2");
                        DocumentSnapshot document = task.getResult();
                        System.out.println(document);
                        if (document.exists()) {
                            System.out.println("БАРЬЕР 3");
                            String name = document.getString("name");
                            String role = document.getString("role");

                            System.out.println("NAME _ " + name);
                            System.out.println("ROLE - " + role);
                            TextView userNameTextView = view.findViewById(R.id.userName);
                            TextView userRoleTextView = view.findViewById(R.id.userRole);

                            userNameTextView.setText(name);
                            userRoleTextView.setText(role);
                        } else {
                            // Документ пользователя не найден
                        }
                    } else {
                        // Ошибка при получении документа пользователя
                    }
                }
            });
        } else {
            // Пользователь не авторизован
        }

    }

}