package com.example.btl_app.View;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_app.Controller.MoneyAdapter;
import com.example.btl_app.Model.MoneyItem;
import com.example.btl_app.R;

import java.util.ArrayList;
import java.util.List;

public class ChangeScreenNextQuestion extends AppCompatActivity {

    RecyclerView recyclerMoney;

    private final String[] PRIZES = {
            "$100", "$200", "$300", "$500", "$1,000",
            "$2,000", "$4,000", "$8,000", "$16,000", "$32,000",
            "$64,000", "$125,000", "$250,000", "$500,000", "$1,000,000"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_screen_next_question);

        recyclerMoney = findViewById(R.id.recyclerMoney);

        // Nhận index hiện tại
        int currentIndex =
                getIntent().getIntExtra("currentIndex", 0);

        setupMoneyBoard(currentIndex);

        // Delay 1.5 giây rồi tự đóng màn hình
        new Handler().postDelayed(() -> {

            setResult(RESULT_OK);

            finish();

        }, 1500);
    }

    private void setupMoneyBoard(int currentIndex) {

        List<MoneyItem> list = new ArrayList<>();

        // Hiển thị từ 1 -> 15
        for (int i = 0; i < 15; i++) {

            // Highlight câu hiện tại
            boolean selected = (i == currentIndex);

            String text =
                    (i + 1) + "   " + PRIZES[i];

            list.add(new MoneyItem(text, selected));
        }

        MoneyAdapter adapter =
                new MoneyAdapter(list);

        recyclerMoney.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerMoney.setAdapter(adapter);

        // Scroll tới câu hiện tại
        recyclerMoney.scrollToPosition(currentIndex);
    }
}