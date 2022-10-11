package com.example.test_login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class login_success extends AppCompatActivity {

    TextView username,password,phone,email,name,gender;

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


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
        //ต้องเอาค่าจาก Login มาใช้
        Intent get_username = getIntent();

        DocumentReference docRef = db.collection("user").document(get_username.getStringExtra("username"));
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

        Button logout = findViewById(R.id.but_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_login_page = new Intent(login_success.this,login_page.class);
                deleteCache(login_success.this);
                startActivity(goto_login_page);
            }
        });
    }
}