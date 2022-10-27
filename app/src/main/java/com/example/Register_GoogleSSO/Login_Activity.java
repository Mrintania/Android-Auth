package com.example.Register_GoogleSSO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_Activity extends AppCompatActivity {
    ///New login Method

    Button login,clear;
    TextView forgotPass;
    int counter = 0;

    //Floating edittext for username
    private void SetupUsernameFloatingLabelError() {
        final TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
        //Username Check
        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 0) {
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

    //Floating edittext for password
    private void SetupPasswordFloatingLabelError() {
        final TextInputLayout floatingPasswordLabel = (TextInputLayout) findViewById(R.id.password_txt_input_layout);
        //Password Check
        floatingPasswordLabel.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 0) {
                    floatingPasswordLabel.setError("Password is required");
                    floatingPasswordLabel.setErrorEnabled(true);
                } else {
                    floatingPasswordLabel.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //SendLoginData to LoginSuccess_Activity
    private void SendUsersLogInData() {
        Intent goto_login_success = new Intent(Login_Activity.this, login_success.class);
        TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
        floatingUsernameLabel.getEditText().getText().toString();
        goto_login_success.putExtra("username", floatingUsernameLabel.getEditText().getText().toString());
        finish();
        startActivity(goto_login_success);
    }

    private void SendAdminLogInData() {
        //Intent goto_admin_login_success = new Intent(Login_Activity.this, Admin_Activity.class);
        Intent goto_admin_login_success = new Intent(Login_Activity.this, Admin_Activity.class);
        TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
        floatingUsernameLabel.getEditText().getText().toString();
        goto_admin_login_success.putExtra("username", floatingUsernameLabel.getEditText().getText().toString());
        finish();
        startActivity(goto_admin_login_success);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SetupUsernameFloatingLabelError();
        SetupPasswordFloatingLabelError();

        //Clear Button
        /*clear = findViewById(R.id.btn_clear);
        clear.setOnClickListener(v -> {
            TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
            TextInputLayout floatingPasswordLabel = (TextInputLayout) findViewById(R.id.password_txt_input_layout);
            floatingUsernameLabel.getEditText().setText("");
            floatingPasswordLabel.getEditText().setText("");
            Toast.makeText(this, "Clear all value", Toast.LENGTH_SHORT).show();
        });*/

        //Login Button
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(v -> {
            TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
            TextInputLayout floatingPasswordLabel = (TextInputLayout) findViewById(R.id.password_txt_input_layout);
            String username = floatingUsernameLabel.getEditText().getText().toString();
            String password = floatingPasswordLabel.getEditText().getText().toString();

/*
            //Get last data in array of password (firebase)
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference users = db.collection("users");
            DocumentReference docRef = users.document(username);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String[] passwordArray = document.get("password").toString().split(",");
                            String lastPassword = passwordArray[passwordArray.length-1];
                            if (lastPassword.equals(password)) {
                                if (username.equals("admin")) {
                                    SendAdminLogInData();
                                } else {
                                    SendUsersLogInData();
                                }
                            } else {
                                counter++;
                                if (counter == 3) {
                                    login.setEnabled(false);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                                    builder.setTitle("Error");
                                    builder.setMessage("You have entered wrong password 3 times! Please try again later!");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    builder.show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                                    builder.setTitle("Error");
                                    builder.setMessage("Wrong password! Please try again!");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                            builder.setTitle("Error");
                            builder.setMessage("User does not exist! Please try again!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    } else {
                        Toast.makeText(Login_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });*/

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                DocumentReference docRef = db.collection("user").document(username);
                DocumentReference docAdmin = db.collection("admin").document(username);
                //check user is admin or not
                docAdmin.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String admin_password = document.getString("password");
                                if (admin_password.equals(password)) {
                                    Toast.makeText(Login_Activity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    SendAdminLogInData();
                                } else {
                                    Toast.makeText(Login_Activity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //check user is user or not
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                //check user password
                                                String user_password = document.getString("password");
                                                StringBuilder user_status_builder = new StringBuilder();
                                                //check user password

                                                //check user status
                                                user_status_builder.append(document.get("status"));
                                                //check user status

                                                //ตรวจสอบ Array ของ password ว่ามี password ที่ตรงกับที่ user กรอกมาหรือไม่
                                                /*StringBuilder user_password_builder = new StringBuilder();
                                                user_password_builder.append(document.getString("password"));
                                                String [] passwordArray = user_password_builder.toString().split(",");
                                                String lastPassword = passwordArray[passwordArray.length-1];*/
                                                //END// ตรวจสอบ Array ของ password ว่ามี password ที่ตรงกับที่ user กรอกมาหรือไม่


                                                if (user_password.equals(password) && user_status_builder.toString().equals("1") ) {
                                                    Toast.makeText(Login_Activity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                                    SendUsersLogInData();
                                                } else if(user_password.equals(password) && user_status_builder.toString().equals("0")){
                                                    alterDialog();
                                                } else {
                                                    counter = counter + 1;
                                                    if (!user_password.equals(password) && user_status_builder.toString().equals("1") && counter<5) {
                                                        Toast.makeText(Login_Activity.this, "Wrong Password " + counter + " Time", Toast.LENGTH_SHORT).show();
                                                    } else if(counter<5 && user_password.equals(password) && user_status_builder.toString().equals("0")){
                                                        alterDialog();
                                                    }else {
                                                        db.collection("user").document(username).update("status", "0");
                                                        //Toast.makeText(Login_Activity.this, "You have been locked out", Toast.LENGTH_SHORT).show();
                                                        alterDialog();
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(Login_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(Login_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(Login_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Forgot password Button
        forgotPass = findViewById(R.id.btn_forgot_password);
        forgotPass.setOnClickListener(v -> {
                Intent goto_forgot_password = new Intent(Login_Activity.this, Forgot_Activity.class);
                startActivity(goto_forgot_password);

        });
    }

    private void alterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
        builder.setTitle("Login Failed");
        builder.setMessage("You Account is LOCK !!! Please wait for admin to approve or forgot password");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Forgot Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                TextInputLayout username_layout = findViewById(R.id.username_txt_input_layout);
                String username = username_layout.getEditText().getText().toString();

                Intent goto_forgot_password = new Intent(Login_Activity.this, Forgot_Activity.class);
                goto_forgot_password.putExtra("username", username);
                startActivity(goto_forgot_password);
            }
        });
        builder.show();
    }

}