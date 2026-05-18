package com.example.btl_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_app.R;
import com.example.btl_app.View.HomeUser;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class StatistcScreen extends AppCompatActivity {

    private TextView txtTotalGame, txtTotalWins, txtTotalLoses, txtHighestScore, txtAverageScore, txtCorrectAnswers, txtWrongAnswers;
    private Button btnBackFromStatisticScreenToHomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistc_screen);

        txtTotalGame = findViewById(R.id.totalGames);
        txtAverageScore = findViewById(R.id.averageScore);
        txtCorrectAnswers = findViewById(R.id.correctAnswers);
        txtHighestScore = findViewById(R.id.highestScore);
        txtWrongAnswers = findViewById(R.id.wrongAnswers);
        txtTotalLoses = findViewById(R.id.totalLoses);
        txtTotalWins = findViewById(R.id.totalWins);
        btnBackFromStatisticScreenToHomeUser = findViewById(R.id.btnBackHomeFromStatisticScreenToHomeUser);

        btnBackFromStatisticScreenToHomeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(StatistcScreen.this, HomeUser.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });

        getAllData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getAllData() {
        FirebaseFirestore _db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        String userId = user.getUid();

        _db.collection("statistics").document(userId).get().addOnSuccessListener(documentSnapshots ->
        {
            txtTotalGame.setText(String.valueOf(documentSnapshots.getLong("totalGames")));
            txtTotalWins.setText(String.valueOf(documentSnapshots.getLong("totalWins")));
            txtTotalLoses.setText(String.valueOf(documentSnapshots.getLong("totalLoses")));
            txtHighestScore.setText(String.valueOf(documentSnapshots.getLong("highestScore")));
            txtAverageScore.setText(String.valueOf(documentSnapshots.getLong("averageScore")));
            txtCorrectAnswers.setText(String.valueOf(documentSnapshots.getLong("correctAnswers")));
            txtWrongAnswers.setText(String.valueOf(documentSnapshots.getLong("totalGames")));

        }).addOnFailureListener(e ->{
            e.printStackTrace();
            Log.e("get Statistics data", "fail");
        });
    }
}