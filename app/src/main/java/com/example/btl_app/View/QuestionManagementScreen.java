package com.example.btl_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_app.Controller.QuestionAdapter;
import com.example.btl_app.Model.Question;
import com.example.btl_app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class QuestionManagementScreen extends AppCompatActivity {

    private ArrayList<Question> questionArrayList;
    private QuestionAdapter questionAdapter;
    private Button btnBackAdminScreenFromQuestionListScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question_management_screen);

        questionArrayList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(this, questionArrayList);
        btnBackAdminScreenFromQuestionListScreen = findViewById(R.id.btnBackAdminScreenFromQuestionListScreen);

        btnBackAdminScreenFromQuestionListScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(QuestionManagementScreen.this, HomeAdmin.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("questions").addSnapshotListener(((value, error) -> {
            if(error != null) return;
            questionArrayList.clear();
            for (QueryDocumentSnapshot documentSnapshot : value)
            {
                Question question = documentSnapshot.toObject(Question.class);
                questionArrayList.add(question);
            }
            questionAdapter.notifyDataSetChanged();
        }));


        ListView lsvQuestion = findViewById(R.id.listViewQuestion);
        lsvQuestion.setAdapter(questionAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}