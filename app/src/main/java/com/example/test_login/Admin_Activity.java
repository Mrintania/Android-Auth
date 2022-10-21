package com.example.test_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import kotlin.jvm.internal.FunctionAdapter;

public class Admin_Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;

    Button  lock, unlock, delete, update, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_login = new Intent(Admin_Activity.this, Login_Activity.class);
                startActivity(goto_login);
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        Query query = db.collection("user");
        FirestoreRecyclerOptions<show_member> options = new FirestoreRecyclerOptions.Builder<show_member>()
                .setQuery(query, show_member.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<show_member, Admin_Activity.Data_holder>(options) {
            @NonNull
            @Override
            public Admin_Activity.Data_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_data, parent, false);
                return new Data_holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Admin_Activity.Data_holder holder, int position, @NonNull show_member model) {
                holder.username.setText(model.getUsername());
                holder.password.setText(model.getPassword());
                holder.email.setText(model.getEmail());
                holder.phone.setText(model.getPhone());
                holder.name.setText(model.getName());
                holder.gender.setText(model.getGender());

                //Convert status from 1 to Active and 0 to Inactive
                if(model.getStatus().equals("1")) {
                    holder.status.setText("Active");

                    //Set button btn_Unlock to false//
                    Button btn_Unlock = holder.itemView.findViewById(R.id.btn_Unlock_user);
                    btn_Unlock.setEnabled(false);

                    //Set button btn_Lock to true//
                    Button btn_Lock = holder.itemView.findViewById(R.id.btn_Lock_user);
                    btn_Lock.setEnabled(true);
                } else {
                    holder.status.setText("Inactive");
                    //If status is Inactive, TextView will be red//
                    holder.status.setTextColor(getResources().getColor(R.color.red));

                    //Set button btn_Unlock to true//
                    Button btn_Unlock = holder.itemView.findViewById(R.id.btn_Unlock_user);
                    btn_Unlock.setEnabled(true);

                    //Set button btn_Lock to false//
                    Button btn_Lock = holder.itemView.findViewById(R.id.btn_Lock_user);
                    btn_Lock.setEnabled(false);
                }
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class Data_holder extends RecyclerView.ViewHolder {

        TextView username, password, email, phone, name, gender,status;

        public Data_holder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.card_username);
            password = itemView.findViewById(R.id.card_password);
            email = itemView.findViewById(R.id.card_email);
            phone = itemView.findViewById(R.id.card_phone);
            name =  itemView.findViewById(R.id.card_name);
            gender = itemView.findViewById(R.id.card_gender);
            status = itemView.findViewById(R.id.card_status);

            //Unlock user//
            itemView.findViewById(R.id.btn_Unlock_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference docRefUpdate = db.collection("user").document(username.getText().toString()); //Get username from TextView
                    docRefUpdate.update("status","1"); //Update status to 1 or Active
                    //Toast.makeText(Admin_Activity.this, "User is now Active", Toast.LENGTH_SHORT).show();
                }
            });

            //Lock user
            itemView.findViewById(R.id.btn_Lock_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference docRefUpdate = db.collection("user").document(username.getText().toString()); //Get username from TextView
                    docRefUpdate.update("status","0"); //Update status to 1 or Active
                    //Toast.makeText(Admin_Activity.this, "User is now LOCKED!!!", Toast.LENGTH_SHORT).show();
                }
            });

            //Update user data
            itemView.findViewById(R.id.btn_update_PerUser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Admin_Activity.this, Admin_edit_user_Activity.class);
                    intent.putExtra("username", username.getText().toString());
                    startActivity(intent);
                }
            });

            //Delete user
            itemView.findViewById(R.id.btn_delete_PerUser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference documentReference = db.collection("user").document(username.getText().toString());
                    documentReference.delete();
                }
            });
        }
    }
}