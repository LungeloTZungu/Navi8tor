package com.varsitycollege.navig8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewsPage extends AppCompatActivity {

    private EditText searchView;
    private TextView location, address;
    private ImageButton add, exit;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String onlineuser;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_page);
        // Initializing varaibles
        recyclerView = findViewById(R.id.reviewsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewsPage.this));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        onlineuser = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Reviews").child(onlineuser);
       add = findViewById(R.id.AddReview);
       exit = findViewById(R.id.Exit);
       // adding functionality to exit button
       exit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(ReviewsPage.this,MainActivity.class);
               startActivity(intent);
           }
       });
        //get map uid from intent
        // adding functionality to add button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Intent intent = new Intent(ReviewsPage.this,Add_Review.class);
                startActivity(intent);
            }
        });
        // applying the Places API
        Places.initialize(getApplicationContext(),"AIzaSyBMg7mc56-uODNtmpcQQU8PR8OtuoRlPcE");
        FirebaseRecyclerOptions<ReviewModel>options = new FirebaseRecyclerOptions.Builder<ReviewModel>()
                .setQuery(reference,ReviewModel.class).build();
// setting the the adapter to adding it up to recyclerview
       adapter = new Adapter(options);
       recyclerView.setAdapter(adapter);


    }


    //---Code attribution---
//Author:MyOnlineTrainingHub
//Title:How to build Interactive Excel Dashboards that Update with ONE CLICK!
//Date:17/11/2022
//Link:https://www.youtube.com/watch?v=ePKC5ZEqeNY

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
}



