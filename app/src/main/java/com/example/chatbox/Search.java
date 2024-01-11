package com.example.chatbox;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbox.Adapter.SearchUserRecyclerViewAdapter;
import com.example.chatbox.Model.UserModel;
import com.example.chatbox.Utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class Search extends AppCompatActivity {
    ImageButton back_btn;
    TextInputEditText username;
    ImageButton search_btn;
    RecyclerView recyclerView;
    SearchUserRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        back_btn = findViewById(R.id.back_btn);
        username = findViewById(R.id.username_edittext);
        search_btn = findViewById(R.id.search_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        username.requestFocus();

        back_btn.setOnClickListener(v-> onBackPressed());

        search_btn.setOnClickListener(v->{
            String searchTerm = Objects.requireNonNull(username.getText()).toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                username.setError("Invalid Username");
                return;
            }
            setupSearchRecyclerView(searchTerm);

        });



    }

    private void setupSearchRecyclerView(String searchTerm) {
        Query query = FirebaseUtils.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchTerm)
                .whereLessThanOrEqualTo("username",searchTerm+'\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUserRecyclerViewAdapter(options,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }

}