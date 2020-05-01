package com.master.networkmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailbox;
    Button submitbtn;
    ImageButton backBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailbox = findViewById(R.id.resetemail);
        submitbtn = findViewById(R.id.pwresetbtn);
        backBtn = findViewById(R.id.backbtn);
        firebaseAuth = FirebaseAuth.getInstance();

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = emailbox.getText().toString().trim();

                if (mail.equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your correct email address", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this,"Password Reset Email Sent! Check Your MailBox.",Toast.LENGTH_LONG).show();
                                Intent b = new Intent(ForgotPasswordActivity.this,ForgotPasswordActivity.class);
                                startActivity(b);
                            }
                            else{
                                Toast.makeText(ForgotPasswordActivity.this,"Not Found Account! Check Email Again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(ForgotPasswordActivity.this,MainActivity.class);
                startActivity(b);
            }
        });
    }
}
