package com.example.btl_app.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app.Model.Question;
import com.example.btl_app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvPrize, tvQuestionContent;
    private Button btnAnsA, btnAnsB, btnAnsC, btnAnsD;
    private ImageButton btn5050, btnExpert;

    private List<Question> allQuestions;
    private List<Question> playQuestions;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;

    private final String[] PRIZES = {
            "$100", "$200", "$300", "$500", "$1,000",
            "$2,000", "$4,000", "$8,000", "$16,000", "$32,000",
            "$64,000", "$125,000", "$250,000", "$500,000", "$1,000,000"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViews();
        loadQuestionsFromJson();

        if (allQuestions != null && !allQuestions.isEmpty()) {
            generatePlayQuestions(); // 🔥 chọn 1 câu mỗi level
            loadCurrentQuestion();
        } else {
            Toast.makeText(this, "Lỗi tải câu hỏi", Toast.LENGTH_SHORT).show();
            finish();
        }

        setAnswerClickListener();
        setHelpClickListener();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvPrize = findViewById(R.id.tvPrize);
        tvQuestionContent = findViewById(R.id.tvQuestionContent);

        btnAnsA = findViewById(R.id.btnAnswerA);
        btnAnsB = findViewById(R.id.btnAnswerB);
        btnAnsC = findViewById(R.id.btnAnswerC);
        btnAnsD = findViewById(R.id.btnAnswerD);

        btn5050 = findViewById(R.id.btn5050);
        btnExpert = findViewById(R.id.btnExpert);
    }

    // 🔥 Load JSON mới (answers + correctIndex)
    private void loadQuestionsFromJson() {
        allQuestions = new ArrayList<>();
        try {
            InputStream is = getAssets().open("questions.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonStr);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                JSONArray ansArray = obj.getJSONArray("answers");
                List<String> answers = new ArrayList<>();

                for (int j = 0; j < ansArray.length(); j++) {
                    answers.add(ansArray.getString(j));
                }

                Question q = new Question(
                        "Q" + i,
                        obj.getString("content"),
                        answers,
                        obj.getInt("correctIndex"),
                        obj.getInt("level")
                );

                allQuestions.add(q);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // 🔥 Chọn 1 câu mỗi level (chuẩn game)
    private void generatePlayQuestions() {
        playQuestions = new ArrayList<>();

        for (int level = 1; level <= 15; level++) {
            List<Question> levelList = new ArrayList<>();

            for (Question q : allQuestions) {
                if (q.getLevel() == level) {
                    levelList.add(q);
                }
            }

            if (!levelList.isEmpty()) {
                Collections.shuffle(levelList);
                playQuestions.add(levelList.get(0));
            }
        }
    }

    private void loadCurrentQuestion() {
        btnAnsA.setVisibility(View.VISIBLE);
        btnAnsB.setVisibility(View.VISIBLE);
        btnAnsC.setVisibility(View.VISIBLE);
        btnAnsD.setVisibility(View.VISIBLE);

        currentQuestion = playQuestions.get(currentQuestionIndex);

        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/15");
        tvPrize.setText("Thưởng: " + PRIZES[currentQuestionIndex]);
        tvQuestionContent.setText(currentQuestion.getContent());

        List<String> ans = currentQuestion.getAnswers();

        btnAnsA.setText("A. " + ans.get(0));
        btnAnsB.setText("B. " + ans.get(1));
        btnAnsC.setText("C. " + ans.get(2));
        btnAnsD.setText("D. " + ans.get(3));
    }

    // 🔥 Click đáp án (dùng index)
    private void setAnswerClickListener() {
        btnAnsA.setOnClickListener(v -> checkAnswer(0));
        btnAnsB.setOnClickListener(v -> checkAnswer(1));
        btnAnsC.setOnClickListener(v -> checkAnswer(2));
        btnAnsD.setOnClickListener(v -> checkAnswer(3));
    }

    private void checkAnswer(int selectedIndex) {
        if (selectedIndex == currentQuestion.getCorrectIndex()) {
            currentQuestionIndex++;

            if (currentQuestionIndex < 15) {
                showResultDialog("Đúng!", "Bạn muốn tiếp tục?", true, false);
            } else {
                showResultDialog("Chiến thắng!", "Bạn đã trở thành triệu phú!", true, true);
            }
        } else {
            int correct = currentQuestion.getCorrectIndex();
            showResultDialog("Sai!",
                    "Đáp án đúng là: " + (char) ('A' + correct),
                    false, false);
        }
    }

    private void showResultDialog(String title, String message, boolean isCorrect, boolean isWin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        if (isWin) {
            builder.setPositiveButton("Kết thúc", (d, w) -> finish());
        } else if (isCorrect) {
            builder.setPositiveButton("Tiếp tục", (d, w) -> loadCurrentQuestion());
        } else {
            builder.setPositiveButton("Thoát", (d, w) -> finish());
        }

        builder.show();
    }

    // 🔥 50:50 + Expert
    private void setHelpClickListener() {

        btn5050.setOnClickListener(v -> {
            btn5050.setEnabled(false);
            btn5050.setAlpha(0.5f);

            int correct = currentQuestion.getCorrectIndex();

            List<Button> wrongButtons = new ArrayList<>();

            if (correct != 0) wrongButtons.add(btnAnsA);
            if (correct != 1) wrongButtons.add(btnAnsB);
            if (correct != 2) wrongButtons.add(btnAnsC);
            if (correct != 3) wrongButtons.add(btnAnsD);

            Collections.shuffle(wrongButtons);

            wrongButtons.get(0).setVisibility(View.INVISIBLE);
            wrongButtons.get(1).setVisibility(View.INVISIBLE);
        });

        btnExpert.setOnClickListener(v -> {
            btnExpert.setEnabled(false);
            btnExpert.setAlpha(0.5f);

            int correct = currentQuestion.getCorrectIndex();

            new AlertDialog.Builder(GameActivity.this)
                    .setTitle("Chuyên gia")
                    .setMessage("Tôi nghĩ đáp án đúng là: " + (char) ('A' + correct))
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}