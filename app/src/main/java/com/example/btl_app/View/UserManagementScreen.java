package com.example.btl_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_app.Controller.UserAdapter;
import com.example.btl_app.Model.User;
import com.example.btl_app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserManagementScreen extends AppCompatActivity {

    private UserAdapter adapter;
    private ArrayList<User> listUser;
    private Button btnBackAdminScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_management_screen);

        listUser = new ArrayList<>();
        adapter = new UserAdapter(this, listUser);
        btnBackAdminScreen = findViewById(R.id.btnBackAdminScreenFromUserListScreen);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(task ->
        {
            if(task.isSuccessful())
            {
                listUser.clear();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    User user = documentSnapshot.toObject(User.class);
                    listUser.add(user);
                }
                adapter.notifyDataSetChanged();
            }else{
                Log.e("Error", "lỗi fetch user", task.getException());
            }

        });


        ListView lsvUser = findViewById(R.id.listViewUser);
        lsvUser.setAdapter(adapter);


        btnBackAdminScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(UserManagementScreen.this, HomeAdmin.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}