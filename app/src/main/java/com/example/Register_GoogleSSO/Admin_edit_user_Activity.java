package com.example.Register_GoogleSSO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Admin_edit_user_Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerView_ed;
    FirestoreRecyclerAdapter adapter1;

    TextView tv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_user);


        //หัว
        tv1 = findViewById(R.id.tv1);
        tv1.setText(getIntent().getStringExtra("username"));




        Intent i = getIntent();
        String get_username = i.getStringExtra("username");

        recyclerView_ed = findViewById(R.id.recyclerViewEditUser);

        Query query_ed = db.collection("user").whereEqualTo("username", get_username);
        //Query query_ed = db.collection("user");
        FirestoreRecyclerOptions<show_member_edit> options_ed = new FirestoreRecyclerOptions.Builder<show_member_edit>()
                .setQuery(query_ed, show_member_edit.class)
                .build();

        adapter1 = new FirestoreRecyclerAdapter<show_member_edit, Admin_edit_user_Activity.Data_holder_ed>(options_ed) {
            @NonNull
            @Override
            public Admin_edit_user_Activity.Data_holder_ed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewV = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_data_edit, parent, false);
                return new Data_holder_ed(viewV);
            }

            @Override
            protected void onBindViewHolder(@NonNull Admin_edit_user_Activity.Data_holder_ed holder, int position, @NonNull show_member_edit model) {
                holder.username.setText(model.getUsername());
                holder.password.setText(model.getPassword());
                holder.email.setText(model.getEmail());
                holder.phone.setText(model.getPhone());
                holder.name.setText(model.getName());
                holder.gender.setText(model.getGender());

            }
        };
        recyclerView_ed.setHasFixedSize(true);
        recyclerView_ed.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_ed.setAdapter(adapter1);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter1.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter1.stopListening();
    }

    private class Data_holder_ed extends RecyclerView.ViewHolder {

        EditText username, password, email, phone, name, gender,status;
        Switch sw_status;

        public Data_holder_ed(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.card_edit_username);
            password = itemView.findViewById(R.id.card_edit_password);
            email = itemView.findViewById(R.id.card_edit_email);
            phone = itemView.findViewById(R.id.card_edit_phone);
            name =  itemView.findViewById(R.id.card_edit_name);
            gender = itemView.findViewById(R.id.card_edit_gender);
            //status = itemView.findViewById(R.id.card_edit_status);

            itemView.findViewById(R.id.btn_update55).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdminUpdate();
                    //Toast.makeText(Admin_edit_user_Activity.this, "Profile has been UPDATE!!!", Toast.LENGTH_SHORT).show();
                    alterDialog();
                }
            });
        }
    }

    //
    void AdminUpdate() {
        EditText UserEdit = (EditText) findViewById(R.id.card_edit_username);
        EditText PassEdit = (EditText) findViewById(R.id.card_edit_password);
        EditText EmailEdit = (EditText) findViewById(R.id.card_edit_email);
        EditText PhoneEdit = (EditText) findViewById(R.id.card_edit_phone);
        EditText NameEdit = (EditText) findViewById(R.id.card_edit_name);

        String UserEdit1 = UserEdit.getText().toString();
        String PassEdit1 = PassEdit.getText().toString();
        String EmailEdit1 = EmailEdit.getText().toString();
        String PhoneEdit1 = PhoneEdit.getText().toString();
        String NameEdit1 = NameEdit.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRefUpdate = db.collection("user").document(UserEdit1);
        docRefUpdate.update("username", UserEdit1);
        docRefUpdate.update("password", PassEdit1);
        docRefUpdate.update("email", EmailEdit1);
        docRefUpdate.update("phone", PhoneEdit1);
        docRefUpdate.update("name", NameEdit1);
    }

    private void alterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account edited");
        builder.setMessage("Profile has been UPDATE!!!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText username = (EditText) findViewById(R.id.card_edit_username);
                String username_put = username.getText().toString();

                Intent intent = new Intent(Admin_edit_user_Activity.this, Admin_Activity.class);
                intent.putExtra("username",username_put);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();
    }
}