package com.shekhar.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class SignupPage extends AppCompatActivity {


    private EditText emailEditText, passwordEditText, confirmPasswordEditText, nameEditText, phoneNumberEditText;

   private Button signupButton , loginButton;


    //Firebase
    private  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        firebaseAuth = FirebaseAuth.getInstance();


        nameEditText = findViewById(R.id.full_name_text_field);
        emailEditText = findViewById(R.id.email_text_field);
        passwordEditText = findViewById(R.id.password_text_field);
        confirmPasswordEditText = findViewById(R.id.confirm_password_text_field);
        signupButton = findViewById(R.id.signup_button);

        loginButton = findViewById(R.id.login_button);




        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = nameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                //validate email and password
                if(fullName.isEmpty()){
                    Toast.makeText(SignupPage.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }else if(email.isEmpty()){
                    Toast.makeText(SignupPage.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }else if(phoneNumber.isEmpty()){
                    Toast.makeText(SignupPage.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }else if(password.isEmpty()){
                    Toast.makeText(SignupPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }else if(confirmPassword.isEmpty()){
                    Toast.makeText(SignupPage.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!password.equals(confirmPassword)){
                    Toast.makeText(SignupPage.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialog dialog = ProgressDialog.show(SignupPage.this, "",
                        "Please wait...", true);

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success then save data in firebase database
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    Map userData =   new HashMap();
                                    userData.put("name",fullName);
                                    userData.put("email",email);
                                    userData.put("phoneNumber",phoneNumber);
                                    userData.put("userId",user.getUid());

                                    //Ref: userRef.doc(userId).set(userData); :::) amele nodu



                                    System.out.println(user.getEmail());

                                    //Dismiss dialog
                                    dialog.dismiss();

                                    Intent intent = new Intent(SignupPage.this, LoginPage.class);
                                    startActivity(intent);

                                } else {
                                    dialog.dismiss();

                                    Toast.makeText(SignupPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                    Toast.makeText(SignupPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( SignupPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });




    }
}