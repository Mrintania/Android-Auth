package com.example.test_login;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register_page extends AppCompatActivity {

    EditText username, password, email, phone, name;
    Button submit, clear;
    RadioButton male, female;
    RadioGroup gender_group;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        username = findViewById(R.id.ed_username);
        password = findViewById(R.id.ed_userpass);
        name = findViewById(R.id.ed_name);
        email = findViewById(R.id.ed_email);
        phone = findViewById(R.id.ed_phone);
        submit = findViewById(R.id.but_register);
        clear = findViewById(R.id.but_clear);
        gender_group = findViewById(R.id.radioGroup_gender);
        male = findViewById(R.id.rad_male);
        female = findViewById(R.id.rad_female);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //แปลงค่าใน Rad-G ให้เป็น String
                int selected = gender_group.getCheckedRadioButtonId();
                RadioButton radio=(RadioButton) findViewById(selected);
                String RadioSex = radio.getText().toString();

                Map<String, Object> user = new HashMap<>();
                user.put("username", username.getText().toString());
                user.put("password", password.getText().toString());
                user.put("name", name.getText().toString());
                user.put("email", email.getText().toString());
                user.put("gender", RadioSex);
                user.put("phone", phone.getText().toString());
                user.put("status", "1");

                db.collection("user").document(username.getText().toString()).set(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(register_page.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(register_page.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                       });

                //Goto-Login-page
                Intent intent = new Intent(register_page.this, login_page.class);
                finish();
                startActivity(intent);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                name.setText("");
                email.setText("");
                phone.setText("");
                gender_group.clearCheck();
            }
        });


    }
}


//Check duplicate username, name
/*
* username
* name lname
* */