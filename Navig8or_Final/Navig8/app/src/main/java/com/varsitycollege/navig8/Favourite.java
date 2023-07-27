package com.varsitycollege.navig8;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//---Code attribution---
//Author:Najmesat
//Title:071 lecture 63 adding data from edit text to firebase database
//Date:16/11/2022
//Link:https:https://www.youtube.com/watch?v=y7CzVRF1vGo&t=2s


public class Favourite extends AppCompatActivity {
//Variable declaration
    private Button btnSubmit;
    private Button btnBack;
    private EditText address;
    private EditText city;
    private EditText country;
    private ListView listView;

//---Code attribution---
//Author:ProgrammingKnowlegde
//Title:Firebase Android Tutorial 4 - Saving Data in Firebase Realtime Database
//Date:15/11/2022
//Link:https://www.youtube.com/watch?v=XhdR2ffYGoE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        address = findViewById(R.id.address);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        listView = findViewById(R.id.listview);


        btnSubmit = findViewById(R.id.btnFav);
        btnBack = findViewById(R.id.btnBack);

//Button that takes you back to the main screen
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Favourite.this, MainActivity.class));
            }
        });
//Sends data typed out to firebase
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textCity = city.getText().toString();
                String textCountry = country.getText().toString();
                String textAddress = address.getText().toString();

                if(textCity.isEmpty()){
                    Toast.makeText(Favourite.this, "No City entered", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Favourites").push().child("Country:").setValue(textCountry);
                    FirebaseDatabase.getInstance().getReference().child("Favourites").push().child("City:").setValue(textCity);
                    FirebaseDatabase.getInstance().getReference().child("Favourites").push().child("Address:").setValue(textAddress);

                    Toast.makeText(Favourite.this, "Details have been added to favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //---Code attribution---
//Author:ProgrammingKnowlegde
//Title:Firebase Android Tutorial 5 - Retrieving Data from Firebase Realtime Database
//Date:16/11/2022
//Link:https:https://www.youtube.com/watch?v=XactTKR0Wfc

        //data read from firebase and is displayed in a list item
        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.item_list,list);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Favourites");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    list.add(snapshot1.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}