package com.example.Register_GoogleSSO;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

                String username1 = username.getText().toString();
                String password1 = password.getText().toString();
                String name1 = name.getText().toString();
                String email1 = email.getText().toString();
                String phone1 = phone.getText().toString();



                if (username1.isEmpty() || password1.isEmpty() || name1.isEmpty() || email1.isEmpty() || phone1.isEmpty()) {
                    Toast.makeText(register_page.this, "มึงต้องใส่ให้หมดนะ", Toast.LENGTH_SHORT).show();
                } else { //เช็ค Username ซ้ำ
                    DocumentReference DocumentUsernameExist = firebaseFirestore.collection("user").document(username1);

                    DocumentUsernameExist.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String username2 = document.getString("username");
                                    if (username1.equals(username2)) {
                                        Toast.makeText(register_page.this, "มึงใส่ Username ซ้ำนะ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //แปลงค่าใน Rad-G ให้เป็น String
                                    int selected = gender_group.getCheckedRadioButtonId();
                                    RadioButton radio = (RadioButton) findViewById(selected);
                                    String RadioSex = radio.getText().toString();

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", username1);
                                    user.put("password", password1);
                                    user.put("name", name1);
                                    user.put("email", email1);
                                    user.put("gender", RadioSex);
                                    user.put("phone", phone1);
                                    user.put("status", "1");

                                    db.collection("user").document(username1).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                    Intent intent = new Intent(register_page.this, Login_Activity.class);
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }//เช็ค Username ซ้ำ
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
