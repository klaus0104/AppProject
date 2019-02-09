package com.example.loginandregistration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    EditText registerEmailEditText, registerPasswordEditText;
    private FirebaseAuth mAuth;

    ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerEmailEditText = findViewById(R.id.registerEmailEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.registerBtn).setOnClickListener(this);
        findViewById(R.id.registrationLinkTextView).setOnClickListener(this);

        registerProgressBar = findViewById(R.id.registerProgressBar);



    }
    private void registerUser() {
        String email = registerEmailEditText.getText().toString().trim();
        String password = registerPasswordEditText.getText().toString().trim();
        //if someone does not enter an email address (error handling)
        if(email.isEmpty()){
            registerEmailEditText.setError("An Email Address is required");
            registerEmailEditText.requestFocus();
            return;
        }
        //if someone enters an invalid email
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmailEditText.setError("Please enter a valid Email Address");
            registerEmailEditText.requestFocus();
            return;

        }
        //if someone does not enter a password (error handling)
        if(password.isEmpty()){
            registerPasswordEditText.setError("A password is required");
            registerPasswordEditText.requestFocus();
            return;

        }
        //password should be more than 6 characters
        if(password.length() < 6){
            registerPasswordEditText.setError("Password should be 6 characters or more");
            registerPasswordEditText.requestFocus();
            return;

        }

        //show a progress bar for HCI
        registerProgressBar.setVisibility(View.VISIBLE);

        //create the user into the google firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                registerProgressBar.setVisibility(View.GONE);
            if(task.isSuccessful()){
                Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
             //for later on

//                Intent intent = new Intent(MainActivity.this, testActivity.class);
//                clear previous activities so that user does not go back to the login page
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
            else {
                //acquired from google firebase (when someone is trying to register with an email that
                // is already in the database) use Auth User Collision
                if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), "This Email Address has already been used", Toast.LENGTH_SHORT).show();
                }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
                registerUser();
                break;

            case R.id.loginLinkTextView:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }
}
