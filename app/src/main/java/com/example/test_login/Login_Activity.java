package com.example.test_login;

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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class Login_Activity extends AppCompatActivity {
    ///New login Method

    Button login,clear,forgotPass;
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
                /*if (s.length() == 0 ) {
                    floatingUsernameLabel.setError("Username is required");
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }*/
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
                /*if (s.length() == 0 ) {
                    floatingPasswordLabel.setError("Password is required");
                    floatingPasswordLabel.setErrorEnabled(true);
                } else {
                    floatingPasswordLabel.setErrorEnabled(false);
                }*/
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
        clear = findViewById(R.id.btn_clear);
        clear.setOnClickListener(v -> {
            TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
            TextInputLayout floatingPasswordLabel = (TextInputLayout) findViewById(R.id.password_txt_input_layout);
            floatingUsernameLabel.getEditText().setText("");
            floatingPasswordLabel.getEditText().setText("");
            Toast.makeText(this, "Clear all value", Toast.LENGTH_SHORT).show();
        });

        //Login Button
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(v -> {
            TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_txt_input_layout);
            TextInputLayout floatingPasswordLabel = (TextInputLayout) findViewById(R.id.password_txt_input_layout);
            String username = floatingUsernameLabel.getEditText().getText().toString();
            String password = floatingPasswordLabel.getEditText().getText().toString();


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
                                                String user_password = document.getString("password");
                                                //String user_status = document.getString("status");
                                                StringBuilder user_status_builder = new StringBuilder();
                                                user_status_builder.append(document.get("status"));

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
                //Check is users
                /*docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot document = task.getResult();

                        StringBuilder st_username = new StringBuilder();
                        StringBuilder st_password = new StringBuilder();
                        StringBuilder st_status = new StringBuilder();

                        st_username.append(document.get("username"));
                        st_password.append(document.get("password"));
                        st_status.append(document.get("status"));

                        if (st_username.toString().equals(username) && st_password.toString().equals(password) && st_status.toString().equals("1")) {
                            Toast.makeText(Login_Activity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            SendLogInData();
                        } else if (st_username.toString().equals(username) && st_password.toString().equals(password) && st_status.toString().equals("0")) {
                            Toast.makeText(Login_Activity.this, "You Account is LOCK !!! Please wait for admin to approve", Toast.LENGTH_SHORT).show();
                        } else {
                            counter = counter + 1;
                            if (counter < 5) {
                                Toast.makeText(Login_Activity.this, "Login Failed " + counter + " Time", Toast.LENGTH_SHORT).show();
                            } else {
                                db.collection("user").document(username).update("status", "0");
                                alterDialog(); //AlertD
                            }
                        }
                    }

                    //AlertDialog for 5 times login failed
                    private void alterDialog() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
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
                });*/
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}