package com.example.btl_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_app.Model.Question;
import com.example.btl_app.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionScreen extends AppCompatActivity {

    private EditText edtContent, edtA, edtB, edtC, edtD, edtCorrect, edtLevel;
    private Button btnSaveQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_question_screen);


        edtContent = findViewById(R.id.edtContent);
        edtA = findViewById(R.id.edtA);
        edtB = findViewById(R.id.edtB);
        edtC = findViewById(R.id.edtC);
        edtD = findViewById(R.id.edtD);
        edtCorrect = findViewById(R.id.edtCorrect);
        edtLevel = findViewById(R.id.edtLevel);
        btnSaveQuestion = findViewById(R.id.btnSaveQuestion);


        btnSaveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateData()) return;

                String content = edtContent.getText().toString().trim();
                String a = edtA.getText().toString().trim();
                String b = edtB.getText().toString().trim();
                String c = edtC.getText().toString().trim();
                String d = edtD.getText().toString().trim();

                int correctIndex = Integer.parseInt(edtCorrect.getText().toString().trim());
                int level = Integer.parseInt(edtLevel.getText().toString().trim());

                List<String> answers = new ArrayList<>();
                answers.add(a);
                answers.add(b);
                answers.add(c);
                answers.add(d);


                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference docRef = db.collection("questions").document(); // auto ID

                String questionId = docRef.getId();

                Question q = new Question(
                        questionId,
                        content,
                        answers,
                        correctIndex,
                        level
                );

                docRef.set(q).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Thêm câu hỏi thành công", Toast.LENGTH_SHORT).show();

                    //chuyển về trang quản lý câu hỏi
                    Intent it = new Intent(AddQuestionScreen.this, QuestionManagementScreen.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);

                }).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateData() {
        if (edtContent.getText().toString().trim().isEmpty()) {
            edtContent.setError("Nhập câu hỏi");
            return false;
        }

        if (edtA.getText().toString().trim().isEmpty()) {
            edtA.setError("Nhập đáp án A");
            return false;
        }

        if (edtB.getText().toString().trim().isEmpty()) {
            edtB.setError("Nhập đáp án B");
            return false;
        }

        if (edtC.getText().toString().trim().isEmpty()) {
            edtC.setError("Nhập đáp án C");
            return false;
        }

        if (edtD.getText().toString().trim().isEmpty()) {
            edtD.setError("Nhập đáp án D");
            return false;
        }

        String correctStr = edtCorrect.getText().toString().trim();
        if (correctStr.isEmpty()) {
            edtCorrect.setError("Nhập đáp án đúng (0-3)");
            return false;
        }

        int correct;
        try {
            correct = Integer.parseInt(correctStr);
            if (correct < 0 || correct > 3) {
                edtCorrect.setError("Chỉ nhập từ 0 đến 3");
                return false;
            }
        } catch (Exception e) {
            edtCorrect.setError("Phải là số");
            return false;
        }

        String levelStr = edtLevel.getText().toString().trim();
        if (levelStr.isEmpty()) {
            edtLevel.setError("Nhập level");
            return false;
        }

        try {
            Integer.parseInt(levelStr);
        } catch (Exception e) {
            edtLevel.setError("Level phải là số");
            return false;
        }

        return true;
    }
}