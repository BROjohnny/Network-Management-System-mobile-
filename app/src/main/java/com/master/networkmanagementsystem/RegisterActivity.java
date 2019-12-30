package com.master.networkmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         //end
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        name = findViewById(R.id.username);
        id = findViewById(R.id.id);
        warn2 = findViewById(R.id.warn);
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
                    mFirebaseAuth.createUserWithEmailAndPassword(emil,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Register Unsuccessful! Please Try Again",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
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
                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }


}
