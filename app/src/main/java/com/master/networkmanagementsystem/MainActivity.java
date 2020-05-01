package com.master.networkmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button login;
    TextView warn,reset;
    ProgressBar progressbar;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        warn = findViewById(R.id.warn);
        progressbar = findViewById(R.id.progressBar);
        reset = findViewById(R.id.forgot);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    finish();
                    Toast.makeText(MainActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(i);
                }
                else {
                    inProgress(false);
                    Toast.makeText(MainActivity.this,"Please login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
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

                else if (!(pwd.isEmpty() && emil.isEmpty())){
                    inProgress(true);
                    mFirebaseAuth.signInWithEmailAndPassword(emil,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                inProgress(false);
                                Toast.makeText(MainActivity.this,"Login Error,Check Email/Password",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                finish();
                                Intent gohome = new Intent(MainActivity.this,HomeActivity.class);
                                //gohome.addFlags(gohome.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(gohome);
                                //finish();return;
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent j = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(j);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent r = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(r);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void inProgress(boolean x){
        if (x){
            progressbar.setVisibility(View.VISIBLE);
            login.setEnabled(false);
            warn.setEnabled(false);
        }
        else {
            progressbar.setVisibility(View.INVISIBLE);
            login.setEnabled(true);
            warn.setEnabled(true);
        }
    }
}
