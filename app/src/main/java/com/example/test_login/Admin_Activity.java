package com.example.test_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Admin_Activity extends AppCompatActivity {

    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    //FirebaseFirestore = FirebaseFirestore.getInstance();

    /*
    * ต้องมี Status ใน Firebase ด้วยดึงมาบอกว่า ปกติ หรือ ไม่ปกติ
    * */

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView username = findViewById(R.id.tv_username);
        String username1 = getIntent().getStringExtra("username");
        DocumentReference docRef = db.collection("user").document(username1);
        username.setText(username1);

        //username.setText(getIntent().getStringExtra("username"));
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLogin = new Intent(Admin_Activity.this, MainActivity.class);
                startActivity(gotoLogin);
            }
        });



    }
}