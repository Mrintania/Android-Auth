package com.example.Register_GoogleSSO;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class google_page extends AppCompatActivity {

    GoogleSignInClient gsc;

    TextView name, email,welcome;
    Button signOut;
    ImageView profile;
    ImageButton profileImg;

    GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_page);

        name = findViewById(R.id.txt_name);
        email = findViewById(R.id.txt_email);
        signOut = findViewById(R.id.but_logout);
        profile = findViewById(R.id.imageView);
        welcome = findViewById(R.id.tv_welcome);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);



        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);

            Glide.with(this).load(personPhoto).into(profile);


        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        welcome.setText("Welcome " + name.getText().toString());

    }


    /// Method to sign out
    private void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                Intent intent = new Intent(google_page.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}