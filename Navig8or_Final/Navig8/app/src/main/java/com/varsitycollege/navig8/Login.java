package com.varsitycollege.navig8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    //variable declaration
    Button Login, SignUp;
    EditText email, Password;
    SignInButton btnGoogleSignIn;
    GoogleSignInClient signIn;
    FirebaseAuth Auth;

    SwitchCompat switchMode;
    SharedPreferences sharedPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //UI components set to variables
        Login = findViewById(R.id.btnLogin);
        SignUp = findViewById(R.id.btnSign);

        email = findViewById(R.id.lEmail);
        Password = findViewById(R.id.lPassword);

        //this will link the buttons with the variables created in this class
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        Auth = FirebaseAuth.getInstance();

        //Code Attribution
        //Links : https://www.youtube.com/watch?v=gD9uQf5UU-g ; https://www.youtube.com/watch?v=E1eqRNTZqDM ; https://firebase.google.com/docs/auth/android/google-signin
        //Authors : Atif Pervaiz ; EasyLearn ; Firebase
        //Used to enable Google Login

        GoogleSignInOptions googleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signIn = GoogleSignIn.getClient(com.varsitycollege.navig8.Login.this, googleOptions);

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() { //this will enable a user to sign into their google account
            @Override
            public void onClick(View view) {
                Intent signInIntent = signIn.getSignInIntent();
                startActivityForResult(signInIntent, 1); // this will take the user to the google signin
            }
        });

        switchMode = findViewById(R.id.switchMode);


//Code attribution
//Links: https://www.youtube.com/watch?v=_hqHA-YSF98 ; https://www.youtube.com/watch?v=-qsHE3TpJqw ; https://www.youtube.com/watch?v=9G1ErQo6dBU
//Authors: Aws Rh ; Coding with Dev ; Android Coding

        sharedPref = getSharedPreferences("light",0); //This is the  default
        Boolean bool = sharedPref.getBoolean("light_mode",false);
        if (bool) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchMode.setChecked(false);
        }

        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton comBtn, boolean isChecked) {
                if (isChecked){ //if button is checked dark mode will be enabled and light mode will be disabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchMode.setChecked(true);
                    SharedPreferences.Editor ed = sharedPref.edit(); //this will edit the shared preference
                    ed.putBoolean("dark_mode",true); //this will set the shared preference as true
                    ed.commit(); //this will commit the edit/change
                }else { //if button is not checked light mode will be enabled and dark mode will be disabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchMode.setChecked(false);
                    SharedPreferences.Editor ed = sharedPref.edit(); //this will edit the shared preference
                    ed.putBoolean("dark_mode",false); //this will set the shared preference as false
                    ed.commit(); //this will commit the edit/change

                }
            }
        });

        Auth = FirebaseAuth.getInstance();

        Login.setOnClickListener(view -> {
            loginUser();                  //Method is called when button is clicked
        });


        //When the signup button is clicked it will take the user to the signup page
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.varsitycollege.navig8.Login.this, signup.class);
                startActivity(intent);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
            // try/catch
            try {
                GoogleSignInAccount log = task.getResult(ApiException.class);
                Toast.makeText(com.varsitycollege.navig8.Login.this, "Sign In Successful", Toast.LENGTH_SHORT).show(); //this will display if the users account details are correct
                FirebaseGoogleAuth(log);
                Intent login = new Intent(com.varsitycollege.navig8.Login.this, MainActivity.class); //this will take the user to their desired page

                startActivity(login); //will perform the intent which will take the user to the Category page
            } catch (ApiException e) {
                Toast.makeText(com.varsitycollege.navig8.Login.this, "Sign In Failed", Toast.LENGTH_SHORT).show(); //this will display if the users account details are incorrect
                FirebaseGoogleAuth(null);
            }
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount gAcc) { //uses firebase too validate google credentials
        //check if the account is null
        if (gAcc != null) {
            AuthCredential authCred = GoogleAuthProvider.getCredential(gAcc.getIdToken(), null);
            Auth.signInWithCredential(authCred).addOnCompleteListener(com.varsitycollege.navig8.Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = Auth.getCurrentUser();
                    }

                }
            });
        }
    }

    private void loginUser() {

        //values from edit boxes are passed to local variables
        String mail = email.getText().toString();
        String Pass = Password.getText().toString();


        if (TextUtils.isEmpty(mail)) {
            email.setError("Email field must not be empty"); //Error handling
            email.requestFocus();
        } else if (TextUtils.isEmpty(Pass)) {
            Password.setError("Password field must not be empty "); //Error handling
            Password.requestFocus();
        } else { //if the fields aren't empty then the nested if statement begins
            Auth.signInWithEmailAndPassword(mail, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //reads email and password
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //if statement checks if task is successful or unsuccessful
                    if (task.isSuccessful()) {
                        startActivity(new Intent(com.varsitycollege.navig8.Login.this, MainActivity.class)); //new activity page is opened
                        Toast.makeText(com.varsitycollege.navig8.Login.this, "Login successful", Toast.LENGTH_LONG); //message notifying the user is outputted

                    } else {
                        Toast.makeText(com.varsitycollege.navig8.Login.this, "Not a registered user. Sign Up?", Toast.LENGTH_SHORT).show(); //message notifying the user is outputted
                    }
                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = Auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(com.varsitycollege.navig8.Login.this, com.varsitycollege.navig8.Login.class));
        }
    }

}