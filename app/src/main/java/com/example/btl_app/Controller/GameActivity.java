package com.example.btl_app.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app.Model.Question;
import com.example.btl_app.R;
import com.example.btl_app.View.ChangeScreenNextQuestion;
import com.example.btl_app.View.HomeUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    //Constants
    private static final int REQUEST_NEXT_QUESTION = 100;
    private static final int[] MONEY_TABLE = {
            100, 200, 300, 500, 1000,
            2000, 4000, 8000, 16000, 32000,
            64000, 125000, 250000, 500000, 1000000
    };
    private TextView tvTimer;
    private CountDownTimer countDownTimer;

    //Firebase
    private FirebaseFirestore db;
    private String userId;
    private String sessionId;
    private String username;

    //UI
    private TextView tvQuestionNumber, tvPrize, tvQuestionContent;
    private Button btnAnsA, btnAnsB, btnAnsC, btnAnsD;
    private ImageButton btn5050, btnExpert, btnStatistic, btnCall, btnMenu;

    //Game state
    private List<Question> allQuestions;
    private List<Question> playQuestions;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private List<String> lifelinesUsed = new ArrayList<>();
    private boolean isLeavingGame = true;


    //Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initFirebase();
        initViews();
        setAnswerClickListener();
        setHelpClickListener();
        setMenuClickListener();
        loadQuestionsFromFirestore();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
        if (isLeavingGame && sessionId != null) {
            updateGameSession("quit");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEXT_QUESTION && resultCode == RESULT_OK) {
            isLeavingGame = true;
            loadCurrentQuestion();
        }
    }


    //Init

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvPrize = findViewById(R.id.tvPrize);
        tvQuestionContent = findViewById(R.id.tvQuestionContent);
        tvTimer = findViewById(R.id.tvTimer);

        btnAnsA = findViewById(R.id.btnAnswerA);
        btnAnsB = findViewById(R.id.btnAnswerB);
        btnAnsC = findViewById(R.id.btnAnswerC);
        btnAnsD = findViewById(R.id.btnAnswerD);

        btn5050 = findViewById(R.id.btn5050Game);
        btnExpert = findViewById(R.id.btnExpertGame);
        btnStatistic = findViewById(R.id.btnStatisticGame);
        btnCall = findViewById(R.id.btnCallGame);
        btnMenu = findViewById(R.id.ImgMenu);
        SoundManager.playBgMusic(this);

    }


    //Load Data

    private int getMenuMoney() {
        if (currentQuestionIndex <= 5) {
            return 0;
        } else if (currentQuestionIndex <= 10) {
            return MONEY_TABLE[4];
        } else if (currentQuestionIndex <= 14) {
            return MONEY_TABLE[9];
        } else {
            return MONEY_TABLE[14];

        }
    }


    private Button getButtonByIndex(int selectedIndex) {
        switch (selectedIndex) {
            case 0:
                return btnAnsA;

            case 1:
                return btnAnsB;

            case 2:
                return btnAnsC;

            default:
                return btnAnsD;
        }
    }


    private void enableAllButtons() {
        btnAnsA.setEnabled(true);
        btnAnsB.setEnabled(true);
        btnAnsC.setEnabled(true);
        btnAnsD.setEnabled(true);
    }


    private void LoadAnswers() {

        btnAnsA.setVisibility(View.VISIBLE);
        btnAnsB.setVisibility(View.VISIBLE);
        btnAnsC.setVisibility(View.VISIBLE);
        btnAnsD.setVisibility(View.VISIBLE);

        btnAnsA.setEnabled(true);
        btnAnsB.setEnabled(true);
        btnAnsC.setEnabled(true);
        btnAnsD.setEnabled(true);

        btnAnsA.setBackgroundTintList(
                getColorStateList(R.color.blue));

        btnAnsB.setBackgroundTintList(
                getColorStateList(R.color.blue));

        btnAnsC.setBackgroundTintList(
                getColorStateList(R.color.blue));

        btnAnsD.setBackgroundTintList(
                getColorStateList(R.color.blue));
    }

    private void disableAllButtons() {
        btnAnsA.setEnabled(false);
        btnAnsB.setEnabled(false);
        btnAnsC.setEnabled(false);
        btnAnsD.setEnabled(false);
    }

    //set empty data answer
    private void setEmptyDataAnswer() {
        btnAnsA.setText("");
        btnAnsB.setText("");
        btnAnsC.setText("");
        btnAnsD.setText("");
    }

    private void replayGame() {
        isLeavingGame = false;
        currentQuestionIndex = 0;
        correctAnswers = 0;
        wrongAnswers = 0;

        SoundManager.playBgMusic(this);
        lifelinesUsed.clear();

        btn5050.setVisibility(View.VISIBLE);
//        btnExpert.setVisibility(View.VISIBLE);
        btnStatistic.setVisibility(View.VISIBLE);
        btnCall.setVisibility(View.VISIBLE);

        generatePlayQuestions();

        createGameSession();

        LoadAnswers();

        loadCurrentQuestion();
    }

    private void loadQuestionsFromFirestore() {
        allQuestions = new ArrayList<>();

        db.collection("questions").get()
                .addOnSuccessListener(snapshots -> {
                    for (QueryDocumentSnapshot doc : snapshots) {
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
                        Toast.makeText(this, "Lỗi tải câu hỏi", Toast.LENGTH_SHORT).show());
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
                Toast.makeText(this, "Thiếu câu hỏi level " + level, Toast.LENGTH_LONG).show();
            }
        }
    }

    //Game Logic

    private void loadCurrentQuestion() {
        setEmptyDataAnswer();
        //display expert button
        if (currentQuestionIndex == 5) {
            btnExpert.setVisibility(View.VISIBLE);
        }

        currentQuestion = playQuestions.get(currentQuestionIndex);
        List<String> ans = currentQuestion.getAnswers();

        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/15");
        tvPrize.setText("Thưởng: " + getMoney() + "$");
        tvQuestionContent.setText(currentQuestion.getContent());

        LoadAnswers();
        disableAllButtons();

        SharedPreferences prefs = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int gamemode = prefs.getInt("gameMode", 0);
        if (gamemode == 0) {
            tvTimer.setVisibility(View.GONE);
        } else {
            tvTimer.setVisibility(View.VISIBLE);
            int time = prefs.getInt("questionTime", 30);
            tvTimer.setText(String.valueOf(time));
        }

        btnAnsA.setText("A. " + ans.get(0));
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                btnAnsB.setText("B. " + ans.get(1));
            }
        }, 1000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                btnAnsC.setText("C. " + ans.get(2));
            }
        }, 2000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                btnAnsD.setText("D. " + ans.get(3));
                startTimer();
            }
        }, 3000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                enableAllButtons();
            }
        }, 3100);
    }


    private void resetAnswerButtons() {
        btnAnsA.setVisibility(View.VISIBLE);
        btnAnsB.setVisibility(View.VISIBLE);
        btnAnsC.setVisibility(View.VISIBLE);
        btnAnsD.setVisibility(View.VISIBLE);
    }

    private void setAnswerClickListener() {
        btnAnsA.setOnClickListener(v -> checkAnswer(0));
        btnAnsB.setOnClickListener(v -> checkAnswer(1));
        btnAnsC.setOnClickListener(v -> checkAnswer(2));
        btnAnsD.setOnClickListener(v -> checkAnswer(3));
    }

    private void checkAnswer(int selectedIndex) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        SoundManager.playClickSound(this);

        disableAllButtons();

        Button selectedButton = getButtonByIndex(selectedIndex);

        // Chọn đáp án -> vàng
        selectedButton.setBackgroundTintList(
                getColorStateList(android.R.color.holo_orange_light));

        AlphaAnimation blink =
                new AlphaAnimation(0.3f, 1.0f);

        blink.setDuration(300);

        blink.setRepeatMode(AlphaAnimation.REVERSE);

        blink.setRepeatCount(3);

        selectedButton.startAnimation(blink);

        new android.os.Handler().postDelayed(() -> {

            int correctIndex = currentQuestion.getCorrectIndex();

            Button correctButton = getButtonByIndex(correctIndex);

            // Đáp án đúng -> xanh
            correctButton.setBackgroundTintList(
                    getColorStateList(android.R.color.holo_green_light));

            new android.os.Handler().postDelayed(() -> {

                if (selectedIndex == correctIndex) {

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

                        startActivityForResult(intent, 100);

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

                    showWinDialog();
                }

            }, 1200);

        }, 700);

    }

    //Dialog

    private void showDialogForEachHelper(String str) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.help_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView txtDes = dialog.findViewById(R.id.txtDes);

        txtDes.setText(str);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showWinDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView txtMessage = dialog.findViewById(R.id.txtMessage);

        TextView txtMoney = dialog.findViewById(R.id.txtMoney);

        txtMoney.setText(getMenuMoney() + "$");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();

            db = FirebaseFirestore.getInstance();

            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot ->
            {
                if (documentSnapshot.exists()) {
                    username = documentSnapshot.getString("userName");
                    String str = "Chúc mừng " + username + " nhận được phần thưởng";

                    txtMessage.setText(str);
                } else {
                    username = "Guest";
                    String str = "Chúc mừng " + username + " nhận được phần thưởng";

                    txtMessage.setText(str);
                }
            }).addOnFailureListener(e ->
            {
                e.printStackTrace();
            });

        }


        dialog.findViewById(R.id.btnContinueWin).setOnClickListener(v -> {
            dialog.dismiss();
            replayGame();
        });

        dialog.findViewById(R.id.btnWin).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeUser.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }


    private void setMenuClickListener() {
        btnMenu.setOnClickListener(v -> showMenuDialog());
    }

    private void showMenuDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_menu_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView txtMoney = dialog.findViewById(R.id.txtMoney);
        txtMoney.setText(getMenuMoney() + "$");

        dialog.findViewById(R.id.btnContinueWin).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnBackHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeUser.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }

    //Lifelines

    private void setHelpClickListener() {

        btn5050.setOnClickListener(v -> {
            lifelinesUsed.add("5050");
            btn5050.setVisibility(View.INVISIBLE);

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
            lifelinesUsed.add("expert");
            btnExpert.setVisibility(View.INVISIBLE);

            int correct = currentQuestion.getCorrectIndex();
            showDialogForEachHelper("Theo Chuyên Gia đáp án là: " + (char) ('A' + correct));
        });

        btnCall.setOnClickListener(v -> {
            lifelinesUsed.add("call");
            btnCall.setVisibility(View.INVISIBLE);

            int correct = currentQuestion.getCorrectIndex();
            int answer = Math.random() < 0.75 ? correct : new Random().nextInt(4);

            showDialogForEachHelper("Bạn bè gợi ý: " + (char) ('A' + answer));
        });

        btnStatistic.setOnClickListener(v -> {
            lifelinesUsed.add("audience");
            btnStatistic.setVisibility(View.INVISIBLE);

            int correct = currentQuestion.getCorrectIndex();
            int correctPercent = 50 + new Random().nextInt(30);
            int remain = 100 - correctPercent;

            int[] percent = new int[4];
            percent[correct] = correctPercent;
            for (int i = 0; i < 4; i++) {
                if (i != correct) percent[i] = remain / 3;
            }

            String msg = "Đáp án A: " + percent[0] + "%\n"
                    + "Đáp án B: " + percent[1] + "%\n"
                    + "Đáp án C: " + percent[2] + "%\n"
                    + "Đáp án D: " + percent[3] + "%";

            showDialogForEachHelper(msg);
        });
    }

    //Firestore

    private void createGameSession() {
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

        db.collection("gamesessions").document(sessionId).update(data);
    }

    private void updateStatistics(boolean isWin) {
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
                        highestScore = Math.max(highestScore, doc.getLong("highestScore").intValue());
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

    // Utils

    private int getMoney() {
        if (currentQuestionIndex == 0) return 0;
        return MONEY_TABLE[currentQuestionIndex - 1];
    }

    private void startTimer() {
        // 1. Lấy cấu hình thời gian
        SharedPreferences prefs = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int questionTimeInSeconds = prefs.getInt("questionTime", 30);
        long timeInMillis = questionTimeInSeconds * 1000L;

        // 2. Hủy timer cũ trước khi tạo cái mới
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // 3. Khởi tạo và chạy Timer mới
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);

                // Cập nhật TextView (Đảm bảo bạn đã có TextView này trong file XML)
                if (tvTimer != null) {
                    tvTimer.setText(String.valueOf(secondsLeft));

                    // Đổi sang màu đỏ khi sắp hết giờ (dưới 5s)
                    if (secondsLeft <= 5) {
                        tvTimer.setTextColor(android.graphics.Color.RED);
                    } else {
                        tvTimer.setTextColor(android.graphics.Color.WHITE);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (tvTimer != null) {
                    tvTimer.setText("0");
                }

                // Khi hết giờ: Vô hiệu hóa nút và xử lý THUA
                disableAllButtons();
                SoundManager.playWrongSound(GameActivity.this);

                wrongAnswers++;
                updateGameSession("lose");
                updateStatistics(false);

//                int correctIndex = currentQuestion.getCorrectIndex();
                showWinDialog();
            }
        }.start();
    }
}