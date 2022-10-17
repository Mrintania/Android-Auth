package com.example.test_login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Forgot_Activity extends AppCompatActivity {

    EditText username,email;
    Button submit,clear;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        username = findViewById(R.id.fed_uname);
        email = findViewById(R.id.fed_email);
        submit = findViewById(R.id.but_submit);
        clear = findViewById(R.id.but_clear);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> user = new HashMap<>();
                if (username.getText().toString().equals("username")){
                    DocumentReference docRef = db.collection("user").document(username.getText().toString());

                }

                user.put("status", "1");



                //Forgot password
                /*
                 * username, email
                 * update status = 1
                 * check edittext match datastore
                 * */
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                email.setText("");
                Toast.makeText(Forgot_Activity.this, "Clear all data", Toast.LENGTH_SHORT).show();
            }
        });


    }
}