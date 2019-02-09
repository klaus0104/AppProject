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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText loginEmailEditText, loginPasswordEditText;
    ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //OnClickListener for registrationLinkText View
        loginEmailEditText = findViewById(R.id.registerEmailEditText);
        loginPasswordEditText = findViewById(R.id.registerPasswordEditText);
        loginProgressBar = findViewById(R.id.registerProgressBar);

        findViewById(R.id.registrationLinkTextView).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    private void userLogin() {
        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();
        //if someone does not enter an email address (error handling)
        if(email.isEmpty()){
            loginEmailEditText.setError("An Email Address is required");
            loginEmailEditText.requestFocus();
            return;
        }
        //if someone enters an invalid email
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmailEditText.setError("Please enter a valid Email Address");
            loginEmailEditText.requestFocus();
            return;

        }
        //if someone does not enter a password (error handling)
        if(password.isEmpty()){
            loginPasswordEditText.setError("A password is required");
            loginPasswordEditText.requestFocus();
            return;

        }
        //password should be more than 6 characters
        if(password.length() < 6){
            loginPasswordEditText.setError("Password should be 6 characters or more");
            loginPasswordEditText.requestFocus();
            return;

        }

        //need another error handler for when user enters wrong password

        //show a progress bar for HCI
        loginProgressBar.setVisibility(View.VISIBLE);

        // lets us login to the actual app
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //when task is complete remove progress bar
                loginProgressBar.setVisibility(View.GONE);

                if(task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, testActivity.class);
                    //clear previous activities so that user does not go back to the login page
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                    else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            // when this text view is clicked it will bring us to registration page

            case R.id.registrationLinkTextView:
                startActivity(new Intent(this, Registration.class));
                break;


            // when login button is clicked it will take us to main page of app

            case R.id.loginBtn:
                userLogin();
                break;

        }
    }
}
