package com.example.btl_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.app.Dialog;
import android.view.Window;
import android.view.ViewGroup;

import com.example.btl_app.View.HomeUser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app.Model.Question;
import com.example.btl_app.R;
import com.example.btl_app.View.ChangeScreenNextQuestion;
import com.google.firebase.firestore.*;

import java.util.*;

public class GameActivity extends AppCompatActivity {


    private String sessionId;
    private List<String> lifelinesUsed = new ArrayList<>();
    private int correctAnswers = 0;
    private int wrongAnswers = 0;



    private TextView tvQuestionNumber, tvPrize, tvQuestionContent;
    private Button btnAnsA, btnAnsB, btnAnsC, btnAnsD;
    private ImageButton btn5050, btnExpert, btnStatistic, btnCall, btnMenu;


    private List<Question> allQuestions;
    private List<Question> playQuestions;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;

    private boolean isLeavingGame = true;

    // Turn on music
    @Override
    protected void onResume() {
        super.onResume();
        SoundManager.playBgMusic(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundManager.stopBgMusic();
    }
    // LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViews();
        loadQuestionsFromFirestore();

        setAnswerClickListener();
        setHelpClickListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        super.onDestroy();

        if (isLeavingGame && sessionId != null) {
            updateGameSession("quit");
        }
    }

    // INIT UI
    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvPrize = findViewById(R.id.tvPrize);
        tvQuestionContent = findViewById(R.id.tvQuestionContent);

        btnAnsA = findViewById(R.id.btnAnswerA);
        btnAnsB = findViewById(R.id.btnAnswerB);
        btnAnsC = findViewById(R.id.btnAnswerC);
        btnAnsD = findViewById(R.id.btnAnswerD);

        btn5050 = findViewById(R.id.btn5050Game);
        btnExpert = findViewById(R.id.btnExpertGame);
        btnStatistic = findViewById(R.id.btnStatisticGame);
        btnCall = findViewById(R.id.btnCallGame);
        btnMenu = findViewById(R.id.ImgMenu);
    }

    // LOAD DATA
    private void loadQuestionsFromFirestore() {
        allQuestions = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("questions").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Question q = doc.toObject(Question.class);
                        q.setQuestionId(doc.getId());
                        allQuestions.add(q);
                    }

                    if (!allQuestions.isEmpty()) {
                        generatePlayQuestions();
                        createGameSession();
                        loadCurrentQuestion();
                    } else {
                        Toast.makeText(this, "Không có câu hỏi", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi Firestore", Toast.LENGTH_SHORT).show());
    }

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
            } else {
                Toast.makeText(this, "Thiếu level " + level, Toast.LENGTH_LONG).show();
            }
        }
    }

    // GAME LOGIC
    private void loadCurrentQuestion() {

        btnExpert.setVisibility(currentQuestionIndex >= 5 ? View.VISIBLE : View.GONE);

        currentQuestion = playQuestions.get(currentQuestionIndex);

        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/15");
        tvPrize.setText("Thưởng: " + getMoney());
        tvQuestionContent.setText(currentQuestion.getContent());

        List<String> ans = currentQuestion.getAnswers();

        LoadAnswers();

        btnAnsA.setText("A. " + ans.get(0));
        btnAnsB.setText("B. " + ans.get(1));
        btnAnsC.setText("C. " + ans.get(2));
        btnAnsD.setText("D. " + ans.get(3));
    }

    private void setAnswerClickListener() {
        btnAnsA.setOnClickListener(v -> checkAnswer(0));
        btnAnsB.setOnClickListener(v -> checkAnswer(1));
        btnAnsC.setOnClickListener(v -> checkAnswer(2));
        btnAnsD.setOnClickListener(v -> checkAnswer(3));
    }

    private void checkAnswer(int selectedIndex) {

        if (selectedIndex == currentQuestion.getCorrectIndex()) {
            SoundManager.playCorrectSound(this);
            correctAnswers++;
            currentQuestionIndex++;

            if (currentQuestionIndex < 15) {
                updateGameSession("playing");
                Intent intent =
                        new Intent(GameActivity.this,
                                ChangeScreenNextQuestion.class);

                intent.putExtra(
                        "currentIndex",
                        currentQuestionIndex
                );

                isLeavingGame = false;
                startActivityForResult(intent, 100);
                overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
            } else {
                updateGameSession("win");
                updateStatistics(true);
                showWinDialog();
            }

        } else {
            SoundManager.playWrongSound(this);
            wrongAnswers++;

            updateGameSession("lose");
            updateStatistics(false);

            int correct = currentQuestion.getCorrectIndex();

            showResultDialog("Sai!",
                    "Đáp án đúng: " + (char) ('A' + correct),
                    false, false);
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data);

        if (requestCode == 100 &&
                resultCode == RESULT_OK) {

            isLeavingGame = true;

            loadCurrentQuestion();
        }
    }

    private void showResultDialog(String title, String message, boolean isCorrect, boolean isWin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        if (!isWin) {
            builder.setPositiveButton("Về menu", (d, w) -> {

                Intent intent =
                        new Intent(GameActivity.this,
                                HomeUser.class);
                startActivity(intent);

                finish();
            });
            finish();
        }

        builder.show();
    }

    private void showWinDialog() {

        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_dialog);

        dialog.setCancelable(false);

        Window window = dialog.getWindow();

        if (window != null) {

            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            window.setBackgroundDrawableResource(
                    android.R.color.transparent
            );
        }

        Button btnContinue =
                dialog.findViewById(R.id.btnContinueWin);

        btnContinue.setOnClickListener(v -> {

            Intent intent =
                    new Intent(GameActivity.this,
                            HomeUser.class);

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK
            );

            startActivity(intent);

            finish();

            dialog.dismiss();
        });

        dialog.show();
    }

    // LIFELINES
    private void setHelpClickListener() {

        btn5050.setOnClickListener(v -> {
            btn5050.setEnabled(false);
            lifelinesUsed.add("5050");

            int correct = currentQuestion.getCorrectIndex();
            List<Button> wrong = new ArrayList<>();

            if (correct != 0) wrong.add(btnAnsA);
            if (correct != 1) wrong.add(btnAnsB);
            if (correct != 2) wrong.add(btnAnsC);
            if (correct != 3) wrong.add(btnAnsD);

            Collections.shuffle(wrong);
            wrong.get(0).setVisibility(View.INVISIBLE);
            wrong.get(1).setVisibility(View.INVISIBLE);

            btn5050.setVisibility(View.INVISIBLE);
        });

        btnExpert.setOnClickListener(v -> {
            btnExpert.setEnabled(false);
            lifelinesUsed.add("expert");

            int correct = currentQuestion.getCorrectIndex();

            new AlertDialog.Builder(this)
                    .setTitle("Chuyên gia")
                    .setMessage("Đáp án: " + (char) ('A' + correct))
                    .setPositiveButton("Ok", (d, w) -> {
                    })
                    .show();
            btnExpert.setVisibility(View.INVISIBLE);
        });

        btnCall.setOnClickListener(v -> {
            btnCall.setEnabled(false);
            lifelinesUsed.add("call");

            int correct = currentQuestion.getCorrectIndex();
            int answer = Math.random() < 0.75 ? correct : new Random().nextInt(4);

            new AlertDialog.Builder(this)
                    .setTitle("Gọi điện")
                    .setMessage("Chọn: " + (char) ('A' + answer))
                    .setPositiveButton("Ok", (d, w) -> {
                    })
                    .show();
            btnCall.setVisibility(View.INVISIBLE);
        });

        btnStatistic.setOnClickListener(v -> {
            btnStatistic.setEnabled(false);
            lifelinesUsed.add("audience");

            int correct = currentQuestion.getCorrectIndex();

            int correctPercent = 50 + new Random().nextInt(30);
            int remain = 100 - correctPercent;

            int[] percent = new int[4];
            percent[correct] = correctPercent;

            for (int i = 0; i < 4; i++) {
                if (i != correct) {
                    percent[i] = remain / 3;
                }
            }

            String msg =
                    "A: " + percent[0] + "%\n" +
                            "B: " + percent[1] + "%\n" +
                            "C: " + percent[2] + "%\n" +
                            "D: " + percent[3] + "%";

            new AlertDialog.Builder(this)
                    .setTitle("Khán giả")
                    .setMessage(msg)
                    .setPositiveButton("Ok", (d, w) -> {
                    })
                    .show();
            btnStatistic.setVisibility(View.INVISIBLE);
        });
    }

    private void LoadAnswers() {
        btnAnsA.setVisibility(View.VISIBLE);
        btnAnsB.setVisibility(View.VISIBLE);
        btnAnsC.setVisibility(View.VISIBLE);
        btnAnsD.setVisibility(View.VISIBLE);
    }

    // FIRESTORE - SESSION
    private void createGameSession() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference doc = db.collection("gamesessions").document();
        sessionId = doc.getId();

        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("userId", userId);
        data.put("score", 0);
        data.put("currentLevel", 1);
        data.put("money", 0);
        data.put("result", "playing");
        data.put("lifelinesUsed", new ArrayList<>());
        data.put("playedAt", System.currentTimeMillis());
        data.put("isFinished", false);

        doc.set(data);
    }

    private void updateGameSession(String result) {

        if (sessionId == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("score", currentQuestionIndex);
        data.put("currentLevel", currentQuestionIndex + 1);
        data.put("money", getMoney());
        data.put("result", result);
        data.put("lifelinesUsed", lifelinesUsed);
        data.put("isFinished", !result.equals("playing"));

        FirebaseFirestore.getInstance()
                .collection("gamesessions")
                .document(sessionId)
                .update(data);
    }

    // FIRESTORE - STATISTICS
    private void updateStatistics(boolean isWin) {

        String userId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("statistics").document(userId).get()
                .addOnSuccessListener(doc -> {

                    int totalGames = 1;
                    int totalWins = isWin ? 1 : 0;
                    int totalLoses = isWin ? 0 : 1;
                    int highestScore = currentQuestionIndex;
                    int avgScore = currentQuestionIndex;

                    int totalCorrect = correctAnswers;
                    int totalWrong = wrongAnswers;

                    if (doc.exists()) {

                        int oldGames = doc.getLong("totalGames").intValue();

                        totalGames += oldGames;
                        totalWins += doc.getLong("totalWins").intValue();
                        totalLoses += doc.getLong("totalLoses").intValue();

                        highestScore = Math.max(highestScore,
                                doc.getLong("highestScore").intValue());

                        totalCorrect += doc.getLong("correctAnswers").intValue();
                        totalWrong += doc.getLong("wrongAnswers").intValue();

                        int oldAvg = doc.getLong("averageScore").intValue();
                        avgScore = (oldAvg * oldGames + currentQuestionIndex) / (oldGames + 1);
                    }

                    Map<String, Object> data = new HashMap<>();
                    data.put("userId", userId);
                    data.put("totalGames", totalGames);
                    data.put("totalWins", totalWins);
                    data.put("totalLoses", totalLoses);
                    data.put("highestScore", highestScore);
                    data.put("averageScore", avgScore);
                    data.put("correctAnswers", totalCorrect);
                    data.put("wrongAnswers", totalWrong);

                    db.collection("statistics").document(userId).set(data);
                });
    }

    // UTIL
    private int getMoney() {
        int[] money = {
                100, 200, 300, 500, 1000,
                2000, 4000, 8000, 16000, 32000,
                64000, 125000, 250000, 500000, 1000000
        };

        if (currentQuestionIndex == 0) return 0;
        return money[currentQuestionIndex - 1];
    }
}