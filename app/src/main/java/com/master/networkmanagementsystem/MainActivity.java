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

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button register;
    TextView warn;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        warn = findViewById(R.id.warn);
        register.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(MainActivity.this,"Fields are empty!",Toast.LENGTH_SHORT).show();
                }
                else if (!(pwd.isEmpty() && emil.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(emil,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Register Unsuccessful! Please Try Again",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            }                        }
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
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
