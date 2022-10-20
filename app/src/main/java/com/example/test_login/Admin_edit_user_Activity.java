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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Admin_edit_user_Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_user);

        Intent i = getIntent();
        String username = i.getStringExtra("username");

        recyclerView = findViewById(R.id.recyclerViewEditUser);
        Query query = db.document(username).collection("user");
        FirestoreRecyclerOptions<show_member> options = new FirestoreRecyclerOptions.Builder<show_member>()
                .setQuery(query, show_member.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<show_member, Admin_edit_user_Activity.Data_holder>(options) {
            @NonNull
            @Override
            public Admin_edit_user_Activity.Data_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_data_edit, parent, false);
                return new Data_holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Admin_edit_user_Activity.Data_holder holder, int position, @NonNull show_member model) {
                holder.username.setText(model.getUsername());
                holder.password.setText(model.getPassword());
                holder.email.setText(model.getEmail());
                holder.phone.setText(model.getPhone());
                holder.name.setText(model.getName());
                holder.gender.setText(model.getGender());
                holder.status.setText(model.getStatus());

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

    private class Data_holder extends RecyclerView.ViewHolder {

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

        }
    }

    //
    void ald() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.member_data_edit, null);
        builder.setView(view);

        EditText UserEdit = (EditText) findViewById(R.id.card_edit_username);
        EditText PassEdit = (EditText) findViewById(R.id.card_edit_password);
        EditText EmailEdit = (EditText) findViewById(R.id.card_edit_email);
        EditText PhoneEdit = (EditText) findViewById(R.id.card_edit_phone);
        EditText NameEdit = (EditText) findViewById(R.id.card_edit_name);
        EditText Gender = (EditText) findViewById(R.id.card_edit_gender);

        String UserEdit1 = UserEdit.getText().toString();
        String PassEdit1 = PassEdit.getText().toString();
        String EmailEdit1 = EmailEdit.getText().toString();
        String PhoneEdit1 = PhoneEdit.getText().toString();
        String NameEdit1 = NameEdit.getText().toString();


        Button update = view.findViewById(R.id.btn_update55);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRefUpdate = db.collection("user").document();
                docRefUpdate.update("username", UserEdit1);
                docRefUpdate.update("password", PassEdit1);
                docRefUpdate.update("email", EmailEdit1);
                docRefUpdate.update("phone", PhoneEdit1);
                docRefUpdate.update("name", NameEdit1);

            }
        });
        //test
    }

}