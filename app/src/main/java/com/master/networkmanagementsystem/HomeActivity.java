package com.master.networkmanagementsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    Button logout,qr,terminal,embtn;
    FirebaseAuth mFirebaseAuth;
    TextView Humidity,temp;
    DatabaseReference dref;
    String status;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.logout);
        qr = findViewById(R.id.qr);
        terminal = findViewById(R.id.terminal);
        embtn = findViewById(R.id.embtn);
        Humidity = findViewById(R.id.humid);
        temp = findViewById(R.id.tempdata);

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

        terminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = getPackageManager().getLaunchIntentForPackage("com.server.auditor.ssh.client                                                     ");
                startActivity(t);
            }
        });

        embtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:0112052117");
                Intent e = new Intent(Intent.ACTION_DIAL,number);
                startActivity(e);
            }
        });


    }
}
