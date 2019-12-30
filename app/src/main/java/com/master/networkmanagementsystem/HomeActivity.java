package com.master.networkmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button logout,qr;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.logout);
        qr = findViewById(R.id.qr);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logouted = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(logouted);
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(HomeActivity.this, QrActivity.class);
                startActivity(q);
            }
        });

    }
}
