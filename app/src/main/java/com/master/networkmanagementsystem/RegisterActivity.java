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

public class RegisterActivity extends AppCompatActivity {
    EditText email,password,name,id;
    Button register;
    TextView warn2;
    ProgressBar regprogressbar;
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
        name = findViewById(R.id.username);
        id = findViewById(R.id.id);
        warn2 = findViewById(R.id.warn);
        regprogressbar = findViewById(R.id.progressBar3);

        inProgress(false);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emil = email.getText().toString();
                String pwd = password.getText().toString().trim();
                String usr = name.getText().toString();
                String workid = id.getText().toString();
                if(emil.isEmpty()){
                    email.setError("Please enter email!");
                    email.requestFocus();
                }
                else if (pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                if(usr.isEmpty()){
                    name.setError("Please enter user name!");
                    name.requestFocus();
                }
                if(workid.isEmpty()){
                    id.setError("Please enter valid company ID!");
                    id.requestFocus();
                }
                else if (pwd.isEmpty() && emil.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Fields are empty!",Toast.LENGTH_SHORT).show();
                }
                else if (!(pwd.isEmpty() && emil.isEmpty())){
                    inProgress(true);
                    mFirebaseAuth.createUserWithEmailAndPassword(emil,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                //finish();
                                Toast.makeText(RegisterActivity.this,"Register Unsuccessful! Please Try Again",Toast.LENGTH_SHORT).show();
                                inProgress(false);
                                //startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
                            }
                            else {
                                finish();
                                Intent j =new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(j);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        warn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void inProgress(boolean x){
        if (x){
            regprogressbar.setVisibility(View.VISIBLE);
            register.setEnabled(false);
            warn2.setEnabled(false);
        }
        else {
            regprogressbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            warn2.setEnabled(true);
        }
    }


}
