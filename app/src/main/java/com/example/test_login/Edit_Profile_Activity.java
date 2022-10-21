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


        Intent get_username = getIntent();
        DocumentReference docRef = db.collection("user").document(get_username.getStringExtra("username")); //ต้องเอาค่าจาก Login มาใช้
        //username ที่ได้จากหน้า login มาใช้ ในการเรียกข้อมูลจาก Firebase
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.get("name").toString());
                        email.setText(document.get("email").toString());
                        password.setText(document.get("password").toString());
                        phone.setText(document.get("phone").toString());
                    }
                }
            }
        });

        //Test ดึงข้อมูลจากหน้า login-success มาลงใน Edittext
        /*name.setText(get_value.getStringExtra("name"));
        email.setText(get_value.getStringExtra("email"));
        phone.setText(get_value.getStringExtra("phone"));
        password.setText(get_value.getStringExtra("password"));*/
        // ไม่โอเค ต้องเรียกมาจาก Firebase แทน


        //เรียกข้อมูลจาก Firebase
        String username = get_username.getStringExtra("username");
        // DocumentReference docRef = db.collection("user").document(get_username.getStringExtra("username")); //เอา username จากหน้า login มาใช้


        //ส่งค่าไป Firebase
        update = findViewById(R.id.but_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefUpdate = db.collection("user").document(username);
                docRefUpdate.update("name",name.getText().toString());
                docRefUpdate.update("email",email.getText().toString());
                docRefUpdate.update("phone",phone.getText().toString());
                docRefUpdate.update("password",password.getText().toString());
                Intent goto_login_success = new Intent(Edit_Profile_Activity.this, login_success.class);
                goto_login_success.putExtra("username",username);
                startActivity(goto_login_success);
                Toast.makeText(Edit_Profile_Activity.this, "Update Success", Toast.LENGTH_SHORT).show();
            }
        });


        //กลับไปหน้า login_success
        back = findViewById(R.id.but_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Edit_Profile_Activity.this, login_success.class);
                startActivity(back);
            }
        });

    }

    //ต้องเอาข้อมูลส่งไปยังหน้า Login_Success ด้วย
    void TT(){
        Intent goto_login_success = new Intent(Edit_Profile_Activity.this, login_success.class);
        finish();
        //goto_login_success.putExtra("name",name.getText().toString());
        startActivity(goto_login_success);
    }
}