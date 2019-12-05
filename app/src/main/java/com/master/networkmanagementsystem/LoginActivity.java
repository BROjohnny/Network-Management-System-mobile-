package com.master.networkmanagementsystem;

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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button register;
    TextView warn2;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        warn2 = findViewById(R.id.warn);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Please login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        warn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emil = email.getText().toString();
                String pwd = password.getText().toString();
                if(emil.isEmpty()){
                    email.setError("Please enter email!");
                    email.requestFocus();
                }
                else if (pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if (pwd.isEmpty() && emil.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields are empty!",Toast.LENGTH_SHORT).show();
                }
                else if (!(pwd.isEmpty() && emil.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(emil,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login Error,Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent gohome = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(gohome);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        warn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(j);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
