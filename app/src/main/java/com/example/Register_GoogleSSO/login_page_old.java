package com.example.Register_GoogleSSO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_page_old extends AppCompatActivity {

    Button login,clear,forgotPass;
    EditText username,password;

    int counter = 0;

    //Floating edittext for username and password
    private void SetupFloatingLabelError() {
        final TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() > 0 && text.length() <= 4) {
                    floatingUsernameLabel.setError("Username is required");
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    // Check username and password must be correct
    /*public void SendMessage(View view){
        message = message.trim();
        if (message.length() == 0){
            username.setError("Please enter a message");
        }

    }*/


    //send login data to login_success
    private void sendLoginData(){
        Intent goto_login_success = new Intent(login_page_old.this,login_success.class);
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
                Intent goto_ForgotPass = new Intent(login_page_old.this,Forgot_Activity.class);
                startActivity(goto_ForgotPass);
            }
        });

        //clear date in edittext
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                Toast.makeText(login_page_old.this, "Clear all Value", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(login_page_old.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(login_page_old.this, "Login Success", Toast.LENGTH_SHORT).show();
                                sendLoginData();
                            } else if (st_username.toString().equals(username.getText().toString()) && st_password.toString().equals(password.getText().toString()) && st_status.toString().equals("0")) {
                                Toast.makeText(login_page_old.this, "You Account is LOCK !!! Please wait for admin to approve", Toast.LENGTH_SHORT).show();
                            } else {
                                counter = counter + 1;
                                if (counter < 5) {
                                    Toast.makeText(login_page_old.this, "Login Failed " + counter + " Time", Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("user").document(username.getText().toString()).update("status", "0");
                                    alterDialog(); //AlertD
                                }
                        /*if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                            Toast.makeText(login_page_old.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.get("password").toString().equals(password.getText().toString())){
                                        Toast.makeText(login_page_old.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        sendLoginData();
                                    }
                                    else if (password == null){
                                        Toast.makeText(login_page_old.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(login_page_old.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(login_page_old.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }*/
                            }
                        }

                        private void alterDialog() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(login_page_old.this);
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