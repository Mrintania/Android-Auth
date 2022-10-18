package com.example.test_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class Forgot_Activity extends AppCompatActivity {

    EditText username,email;
    Button submit,clear;

    private void AldSuccess(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Your account is Unlocked");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Forgot_Activity.this,Login_Activity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        username = findViewById(R.id.fed_uname);
        email = findViewById(R.id.fed_email);
        submit = findViewById(R.id.but_submit);
        clear = findViewById(R.id.but_clear);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(Forgot_Activity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference docRef = db.collection("user").document(username.getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            DocumentSnapshot documentSnapshot = task.getResult();

                            StringBuilder st_username = new StringBuilder();
                            StringBuilder st_email = new StringBuilder();
                            StringBuilder st_status = new StringBuilder();

                            st_username.append(documentSnapshot.get("username"));
                            st_email.append(documentSnapshot.get("email"));
                            st_status.append(documentSnapshot.get("status"));

                            if (st_username.toString().equals(username.getText().toString()) && st_email.toString().equals(email.getText().toString()) && st_status.toString().equals("0")) {
                                db.collection("user").document(username.getText().toString()).update("status", "1");
                                AldSuccess();
                            } else if (st_username.toString().equals(username.getText().toString()) && st_email.toString().equals(email.getText().toString()) && st_status.toString().equals("1")) {
                                Toast.makeText(Forgot_Activity.this, "Your account is already unlocked", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!st_username.toString().equals(username.getText().toString())) {
                                    Toast.makeText(Forgot_Activity.this, "Username is incorrect", Toast.LENGTH_SHORT).show();
                                } else if (!st_email.toString().equals(email.getText().toString())) {
                                    Toast.makeText(Forgot_Activity.this, "Email is incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Forgot_Activity.this, "Username and Email is incorrect", Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(Forgot_Activity.this, "Username or Email is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                //user.put("status", "1");

                //Forgot password
                /*
                 * username, email
                 * update status = 1
                 * check edittext match datastore
                 * */
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                email.setText("");
                Toast.makeText(Forgot_Activity.this, "Clear all data", Toast.LENGTH_SHORT).show();
            }
        });


    }


}