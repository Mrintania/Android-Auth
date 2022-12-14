package com.example.Register_GoogleSSO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_success extends AppCompatActivity {

    TextView username,password,phone,email,name,gender;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        username = findViewById(R.id.txt_username);
        password = findViewById(R.id.txt_password);
        phone = findViewById(R.id.txt_pn);
        email = findViewById(R.id.txt_email);
        name = findViewById(R.id.txt_name);
        gender = findViewById(R.id.txt_gender);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //เรียกข้อมูลจาก Firebase
        Intent get_username = getIntent();

        DocumentReference docRef = db.collection("user").document(get_username.getStringExtra("username")); //ต้องเอาค่าจาก Login มาใช้
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username.setText(document.get("username").toString());
                        password.setText(document.get("password").toString());
                        phone.setText(document.get("phone").toString());
                        email.setText(document.get("email").toString());
                        name.setText(document.get("name").toString());
                        gender.setText(document.get("gender").toString());
                    }
                }
            }
        });

        edit = findViewById(R.id.but_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_EditProfile = new Intent(login_success.this, Edit_Profile_Activity.class);
                goto_EditProfile.putExtra("username", username.getText().toString()); //ส่งค่าไปหน้า Edit_Profile_Activity
                startActivity(goto_EditProfile);
            }
        });

        Button logout = findViewById(R.id.but_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent goto_login_page = new Intent(login_success.this,Login_Activity.class);
                startActivity(goto_login_page);
            }
        });
    }
}