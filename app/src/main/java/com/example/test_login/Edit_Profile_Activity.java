package com.example.test_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Edit_Profile_Activity extends AppCompatActivity {

    EditText name,email, phone, password;
    Button update,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        phone = findViewById(R.id.et_phone);
        password = findViewById(R.id.et_password);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //เรียกข้อมูลจาก Firebase
        Intent get_username = getIntent();
        DocumentReference docRef = db.collection("user").document(get_username.getStringExtra("username")); //เอา username จากหน้า login มาใช้

        //username ที่ได้จากหน้า login มาใช้ ในการเรียกข้อมูลจาก Firebase
        /*docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.get("name").toString());
                        email.setText(document.get("email").toString());
                    }
                }
            }
        });*/


        //Test ดึงข้อมูลจากหน้า login-success มาลงใน Edittext
        Intent get_value = getIntent();
        name.setText(get_value.getStringExtra("name"));
        email.setText(get_value.getStringExtra("email"));
        phone.setText(get_value.getStringExtra("phone"));
        password.setText(get_value.getStringExtra("password"));




        //ส่งค่าไป Firebase
        update = findViewById(R.id.but_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefUpdate = db.collection("user").document(get_username.getStringExtra("username"));


                docRefUpdate.update("name",name.getText().toString());
                docRefUpdate.update("email",email.getText().toString());
                docRefUpdate.update("phone",phone.getText().toString());
                docRefUpdate.update("password",password.getText().toString());



                Toast.makeText(Edit_Profile_Activity.this, "Profile has updated", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });


        //กลับไปหน้า login_success
        back = findViewById(R.id.but_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void onBackPressed(){
        Intent goto_login_success = new Intent(Edit_Profile_Activity.this, login_success.class);
        startActivity(goto_login_success);
    }
}