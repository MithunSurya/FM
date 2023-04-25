package com.shekhar.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {


    private EditText emailEditText, passwordEditText;
    private Button loginButton, forgotButton, signupButton;

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);
        emailEditText = findViewById(R.id.email_field);
        passwordEditText = findViewById(R.id.password_field);
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        loginButton = findViewById(R.id.login_button);
        forgotButton = findViewById(R.id.forgot_password_button);
        signupButton = findViewById(R.id.signup_button);

        //Firebase
        mAuth = FirebaseAuth.getInstance();


        //Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                //validate email and password
               if(email.isEmpty()){
                   Toast.makeText(LoginPage.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                   return;
               }else if(password.isEmpty()){
                    Toast.makeText(LoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Login with email and password
                //Show loading
                ProgressDialog dialog = ProgressDialog.show(LoginPage.this, "",
                        "Please wait...", true);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    System.out.println(user.getEmail());
                                    dialog.dismiss();

                                    //After login
                                    Intent intent = new Intent(LoginPage.this, Homepage.class);
                                    startActivity(intent);
                                 } else {
                                    // If sign in fails
                                    Log.w(TAG, "Failed", task.getException());
                                    Toast.makeText(LoginPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Toast.makeText(LoginPage.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }
        });


        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recover password
                String email = emailEditText.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(LoginPage.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialog dialog = ProgressDialog.show(LoginPage.this, "",
                        "Please wait...", true);
               mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");

                                    Toast.makeText(LoginPage.this, "Email sent to "+email, Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(LoginPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send user to signup/register page
                Intent intent = new Intent(LoginPage.this, SignupPage.class);
                startActivity(intent);
            }
        });



    }
}