package com.example.test_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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

    Button login,clear,forgotPass;
    EditText username,password;

    int counter = 0;

    //send login data to login_success
    private void sendLoginData(){
        Intent goto_login_success = new Intent(login_page.this,login_success.class);
        goto_login_success.putExtra("username",username.getText().toString()); //ส่งค่าไปหน้า login_success
        finish();
        startActivity(goto_login_success);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        login = findViewById(R.id.but_login);
        clear = findViewById(R.id.but_clear);
        username = findViewById(R.id.ed_username);
        password = findViewById(R.id.ed_userpass);
        forgotPass = findViewById(R.id.but_ForgotPassword);


        //Forgot password
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_ForgotPass = new Intent(login_page.this,Forgot_Activity.class);
                startActivity(goto_ForgotPass);
            }
        });

        //clear date in edittext
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                Toast.makeText(login_page.this, "Clear all Value", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(login_page.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference docRef = db.collection("user").document(username.getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            DocumentSnapshot document = task.getResult();

                            StringBuilder st_username = new StringBuilder();
                            StringBuilder st_password = new StringBuilder();
                            StringBuilder st_status = new StringBuilder();

                            st_username.append(document.get("username"));
                            st_password.append(document.get("password"));
                            st_status.append(document.get("status"));

                            if (st_username.toString().equals(username.getText().toString()) && st_password.toString().equals(password.getText().toString()) && st_status.toString().equals("1")) {
                                Toast.makeText(login_page.this, "Login Success", Toast.LENGTH_SHORT).show();
                                sendLoginData();
                            } else if (st_username.toString().equals(username.getText().toString()) && st_password.toString().equals(password.getText().toString()) && st_status.toString().equals("0")) {
                                Toast.makeText(login_page.this, "You Account is LOCK !!! Please wait for admin to approve", Toast.LENGTH_SHORT).show();
                            } else {
                                counter = counter + 1;
                                if (counter < 5) {
                                    Toast.makeText(login_page.this, "Login Failed " + counter + " Time", Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("user").document(username.getText().toString()).update("status", "0");
                                    alterDialog(); //AlertD
                                }
                        /*if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                            Toast.makeText(login_page.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.get("password").toString().equals(password.getText().toString())){
                                        Toast.makeText(login_page.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        sendLoginData();
                                    }
                                    else if (password == null){
                                        Toast.makeText(login_page.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(login_page.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(login_page.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }*/
                            }
                        }

                        private void alterDialog() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(login_page.this);
                            builder.setTitle("Login Failed");
                            builder.setMessage("You Account is LOCK !!! Please wait for admin to approve");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    });
                }
            }
        });
    }
}