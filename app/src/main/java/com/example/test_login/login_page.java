package com.example.test_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class login_page extends AppCompatActivity {

    Button login,clear;
    EditText username,password;


    /*public static void deleteCache(Context context) {
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
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        login = findViewById(R.id.but_login);
        clear = findViewById(R.id.but_clear);
        username = findViewById(R.id.ed_username);
        password = findViewById(R.id.ed_userpass);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("user").document(username.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(document.get("password").toString().equals(password.getText().toString())){
                                    Toast.makeText(login_page.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent goto_login_success = new Intent(login_page.this,login_success.class);
                                    goto_login_success.putExtra("username",username.getText().toString());
                                    startActivity(goto_login_success);
                                }
                                else if (password == null){
                                    Toast.makeText(login_page.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(login_page.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(login_page.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });








    }
}