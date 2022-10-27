package com.example.Register_GoogleSSO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener{
    ///New login Method

    //Google Sign In
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MainActivity";
    ImageButton googleBtn;
    //Google Sign In

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

        getCacheDir().delete();

        //Google Sign In
        googleBtn = findViewById(R.id.img_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        //Toast.makeText(MainActivity.this, "Something went wrong Connection", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleBtn.setOnClickListener(this);

        //Google Sign In


        SetupUsernameFloatingLabelError();
        SetupPasswordFloatingLabelError();

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
                                                //check user password
                                                String user_password = document.getString("password");
                                                StringBuilder user_status_builder = new StringBuilder();
                                                //check user password

                                                //check user status
                                                user_status_builder.append(document.get("status"));
                                                //check user status

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

        //Register Button
        TextView register = findViewById(R.id.btn_register);
        register.setOnClickListener(v -> {
            Intent goto_register = new Intent(Login_Activity.this, register_page.class);
            startActivity(goto_register);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_google:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            navigateToGoogleActivity();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Something went wrong SignIn", Toast.LENGTH_SHORT).show();
        }

    }

    private void navigateToGoogleActivity() {
        Intent intent = new Intent(this, google_page.class);
        startActivity(intent);
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