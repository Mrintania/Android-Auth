package com.example.Register_GoogleSSO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShowUser_Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        recyclerView = findViewById(R.id.recyclerView2);
        Query query = db.collection("user");
        FirestoreRecyclerOptions<show_member> options = new FirestoreRecyclerOptions.Builder<show_member>()
                .setQuery(query, show_member.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<show_member, Data_holder>(options) {
            @NonNull
            @Override
            public Data_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_data, parent, false);
                return new Data_holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Data_holder holder, int position, @NonNull show_member model) {
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
}
