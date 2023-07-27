package com.varsitycollege.navig8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Add_Review extends AppCompatActivity {
private EditText location,review;
private RatingBar ratingBar;
private Button add,exit;
private FirebaseAuth mAuth;
private FirebaseUser user;
private String onlineuser;
private RecyclerView recyclerView;
DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        // Initializing variables
        location = findViewById(R.id.findLocation);
        review = findViewById(R.id.Wrtie_review);
        add = findViewById(R.id.AddReview);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
         onlineuser = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Reviews");
        exit = findViewById(R.id.Cancel);
        Places.initialize(getApplicationContext(),"AIzaSyBMg7mc56-uODNtmpcQQU8PR8OtuoRlPcE");
        location.setFocusable(false);
        FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(Add_Review.this);

                startActivityForResult(intent,100);
            }
        });
// addding functionality
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uid.getEmail();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                String date = sdf.format(c.getTime());
               ReviewModel reviewModel = new ReviewModel( review.getText().toString(),date,location.getText().toString(),email);
                 reference.child(location.getText().toString()).setValue(reviewModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                         Toast.makeText(Add_Review.this, "Review was posted", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(Add_Review.this,ReviewsPage.class);
                         startActivity(intent);
                     }
                 });
            }
        });
// adding functionality
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Review.this,ReviewsPage.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            location.setText(place.getName());

        } else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }




}
