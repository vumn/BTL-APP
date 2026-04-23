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

    private final String[] PRIZES = {"$100", "$200", "$300", "$500", "$1,000", "$2,000", "$4,000", "$8,000", "$16,000", "$32,000", "$64,000", "$125,000", "$250,000", "$500,000", "$1,000,000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        initViews();
        loadQuestionsFromJson();

        if (allQuestions != null && !allQuestions.isEmpty()) {
            Collections.shuffle(allQuestions);
            playQuestions = allQuestions.subList(0, 15);
            loadCurrentQuestion();
        } else {
            Toast.makeText(this, "Lỗi tải dữ liệu câu hỏi", Toast.LENGTH_SHORT).show();
            finish();
        }

        setAnswerClickListener();
        setHelpClickListener();

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_game), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void setAnswerClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAnswer = "";
                if (v.getId() == R.id.btnAnswerA) selectedAnswer = "A";
                else if (v.getId() == R.id.btnAnswerB) selectedAnswer = "B";
                else if (v.getId() == R.id.btnAnswerC) selectedAnswer = "C";
                else if (v.getId() == R.id.btnAnswerD) selectedAnswer = "D";

                checkAnswer(selectedAnswer);
            }
        };
        btnAnsA.setOnClickListener(listener);
        btnAnsB.setOnClickListener(listener);
        btnAnsC.setOnClickListener(listener);
        btnAnsD.setOnClickListener(listener);
    }

    private void loadCurrentQuestion() {
        btnAnsA.setVisibility(View.VISIBLE);
        btnAnsB.setVisibility(View.VISIBLE);
        btnAnsC.setVisibility(View.VISIBLE);
        btnAnsD.setVisibility(View.VISIBLE);

        currentQuestion = playQuestions.get(currentQuestionIndex);

        tvQuestionNumber.setText("Câu: " + (currentQuestionIndex + 1) + "/15");
        tvPrize.setText("Thưởng: " + PRIZES[currentQuestionIndex]);
        tvQuestionContent.setText(currentQuestion.getContent());

        btnAnsA.setText("A. " + currentQuestion.getAnsA());
        btnAnsB.setText("B. " + currentQuestion.getAnsB());
        btnAnsC.setText("C. " + currentQuestion.getAnsC());
        btnAnsD.setText("D. " + currentQuestion.getAnsD());
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
    private void loadQuestionsFromJson() {
        allQuestions = new ArrayList<>();
        String jsonStr = "";
        try {
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonStr = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Question q = new Question(
                        obj.getString("content"),
                        obj.getString("answerA"),
                        obj.getString("answerB"),
                        obj.getString("answerC"),
                        obj.getString("answerD"),
                        obj.getString("correctAnswer")
                );
                allQuestions.add(q);
            }
        } catch (Exception e) {
            // Hiện thông báo chứa dòng lỗi chi tiết lên màn hình điện thoại
            Toast.makeText(this, "Chi tiết lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            currentQuestionIndex++;
            if (currentQuestionIndex < 15) {
               showResultDialog("Câu trả lời chính xác!", "Bạn có muốn tiếp tục?", true, false);
            } else {
                showResultDialog("Chiến thắng", "Chúc mừng, bạn đã trở thành triệu phú", true, true);
            }
        } else {
            showResultDialog("Rất tiếc!", "Đáp án đúng là: " + currentQuestion.getCorrectAnswer() + ". Chúc bạn may mắn lần sau!", false, false);
        }
    }
    private void showResultDialog(String title, String message, boolean isCorrect, boolean isWin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setCancelable(false); // Ngăn người chơi bấm ra khoảng trống để tắt dialog

        if (isWin) {
            builder.setPositiveButton("Chúc mừng!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        else if (isCorrect) {
            builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadCurrentQuestion();
                }
            });
        }
        else {
            builder.setPositiveButton("Quay lại màn hình chính", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        builder.show();
    }

    // 2 quyền trợ giúp
    private void setHelpClickListener() {
        btn5050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn5050.setEnabled(false);
                btn5050.setAlpha(0.5f);

                String correctAnswer = currentQuestion.getCorrectAnswer();

                List<Button> wrongButtons = new ArrayList<>();
                if (!correctAnswer.equals("A")) wrongButtons.add(btnAnsA);
                if (!correctAnswer.equals("B")) wrongButtons.add(btnAnsB);
                if (!correctAnswer.equals("C")) wrongButtons.add(btnAnsC);
                if (!correctAnswer.equals("D")) wrongButtons.add(btnAnsD);

                Collections.shuffle(wrongButtons);

                wrongButtons.get(0).setVisibility(View.INVISIBLE);
                wrongButtons.get(1).setVisibility(View.INVISIBLE);
            }
        });
        btnExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExpert.setEnabled(false);
                btnExpert.setAlpha(0.5f);

                String correctAnswer = currentQuestion.getCorrectAnswer();
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("Chuyên gia");
                builder.setMessage("Với kinh nghiệm của mình, tôi nghĩ đáp án đúng là: " + correctAnswer);
                builder.setPositiveButton("Cảm ơn", null);
                builder.show();
            }
        });
    }

}