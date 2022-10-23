package com.example.Register_GoogleSSO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Password_Recovery extends AppCompatActivity {

    private void AldSuccess(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Your account is has new password");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goto_login = new Intent(Password_Recovery.this,Login_Activity.class);
                startActivity(goto_login);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText NewPassword,ConPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        NewPassword = findViewById(R.id.ed_Recovery_Password);
        ConPassword = findViewById(R.id.ed_Confirm_Recovery_Password);

        Intent get_username = getIntent();
        get_username.getStringExtra("username");
        String username = get_username.getStringExtra("username");

        Button reset_password = findViewById(R.id.btn_Recovery_Password);
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference docRef = db.collection("user").document(username);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        //ถ้าเจอ username ในฐานข้อมูล
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //เช็คว่า password ใหม่กับยืนยัน password ตรงกันหรือไม่
                                if (checkPassword()) {
                                    //ถ้าตรงกันให้เปลี่ยน password ใหม่
                                    db.collection("user").document(username).update("password", NewPassword.getText().toString());
                                    db.collection("user").document(username).update("status", "1");
                                    AldSuccess();
                                } else {
                                    Toast.makeText(Password_Recovery.this, "Password not match", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Password_Recovery.this, "Username not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Password_Recovery.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }
        });
    }

    //ตรวจสอบ password ใหม่กับยืนยัน password ตรงกันหรือไม่
    private boolean checkPassword() {
        String NewPassword1 = NewPassword.getText().toString();
        String ConPassword1 = ConPassword.getText().toString();
        if (NewPassword1.equals(ConPassword1)) {
            return true;
        } else {
            return false;
        }
    }

}