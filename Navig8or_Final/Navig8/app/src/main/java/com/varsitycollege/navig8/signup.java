package com.varsitycollege.navig8;
//App creators: Mohamed Rajab-ST10116167, Reeselin Pillay-ST10117187,Terell Rangasamy-ST10117009, Fransua Somers-ST10117162, Lungelo Zungu-ST10116993

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {
    //variable declaration
    EditText Email, suPassword;
    //EditText  Number;
    Button sSignUp, Back;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //UI components set to variables
        sSignUp = findViewById(R.id.btnSignUp);
        Back = findViewById(R.id.btnBack);
        Email = findViewById(R.id.sEmail);
        suPassword = findViewById(R.id.sPassword);
        //Number = findViewById(R.id.number);

        Auth = FirebaseAuth.getInstance();




        sSignUp.setOnClickListener(view -> {
            newUser(); //Method called when button is clicked
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(signup.this, Login.class);
                startActivity(i);
            }
        });
    }

    private void newUser(){
        //values passed into local variables
        String email = Email.getText().toString();
        String password = suPassword.getText().toString();
        // String phone = Number.getText().toString();

        if (TextUtils.isEmpty(email)){
            Email.setError("Email field must not be empty"); //Error handling
            Email.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            suPassword.setError("Password field must not be empty "); //Error handling
            suPassword.requestFocus();
            // }else if (TextUtils.isEmpty(phone)){
            //     Number.setError("Phone number field must not be empty ");
            //    Number.requestFocus();

        }else{ //if the fields aren't empty then the nested if statement begins
            Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //user password and email created and stored in firebase
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //if statement checks if task is successful or unsuccessful
                    if (task.isSuccessful()){
                        Toast.makeText(signup.this, "Registration successful",Toast.LENGTH_SHORT).show(); //message notifying the user is outputted
                        startActivity(new Intent(signup.this, Login.class)); //new activity page is opened
                    }else{
                        Toast.makeText(signup.this, "Invalid Email or Password Not Long Enough", Toast.LENGTH_SHORT).show(); //message notifying the user is outputted
                    }
                }
            });
        }
    }

}