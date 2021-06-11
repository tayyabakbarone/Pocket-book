package com.example.application.pocketbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public EditText emailText2,passwordText2,confirmPassword;
    public Button registerButton;
    public TextView pocketbookText2;
    public TextView signInText;
    public FirebaseAuth mFirebaseAuth;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=*])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth= FirebaseAuth.getInstance();
        emailText2 = findViewById(R.id.emailText2);
        passwordText2 = findViewById(R.id.passwordText2);
        signInText = findViewById(R.id.signInText);
        pocketbookText2 = findViewById(R.id.pocketbookText2);
        registerButton = findViewById(R.id.registerButton);
        confirmPassword = findViewById(R.id.confirmPassword);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText2.getText().toString();
                String password = passwordText2.getText().toString();
                if(email.isEmpty()){
                    emailText2.setError("Pleaser enter Email");
                    emailText2.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordText2.setError("Pleaser enter Password");
                    passwordText2.requestFocus();
                }
                else if(password.isEmpty() &&  email.isEmpty())
                    Toast.makeText(RegisterActivity.this,"Fields are empty",Toast.LENGTH_SHORT);

                else if(!(password.isEmpty() &&  email.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (email.toString().trim().matches(emailPattern) && PASSWORD_PATTERN.matcher(password).matches() && password.equals(confirmPassword) ) {
                                if (!task.isSuccessful()) {
                                    mFirebaseAuth.fetchSignInMethodsForEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                                    if (!isNewUser) {
                                                        Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();

                                                    }
                                                    Toast.makeText(RegisterActivity.this, "Sign Up UnSuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            }
                            else if(!password.equals(confirmPassword)){
                                Toast.makeText(RegisterActivity.this, "Password Doesnt Match", Toast.LENGTH_SHORT).show();

                            }
                            else if(!email.toString().trim().matches(emailPattern)) {
                                Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }
                            else if(!PASSWORD_PATTERN.matcher(password).matches()){
                                Toast.makeText(RegisterActivity.this, "Password too weak", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
