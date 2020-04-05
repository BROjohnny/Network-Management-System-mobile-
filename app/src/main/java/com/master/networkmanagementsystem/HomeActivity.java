package com.master.networkmanagementsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView Humid,temp;
    DatabaseReference dref;
    String status;
    private long backPressedTime;
    private Toast backToast;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.logout);
        qr = findViewById(R.id.qr);
        terminal = findViewById(R.id.terminal);
        embtn = findViewById(R.id.embtn);
        Humid = findViewById(R.id.humid);
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
                Intent t = getPackageManager().getLaunchIntentForPackage("com.termux");//com.server.auditor.ssh.client
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


        dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("Humidity").getValue().toString();
                Humid.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Humid.setText("not done");
                String[] para = Humid.getText().toString().split("\\s+");
                Toast.makeText(HomeActivity.this, "" + para.length, Toast.LENGTH_LONG).show();
            }
        });

        dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("Temperature").getValue().toString();
                temp.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                temp.setText("not done");
                String[] para = temp.getText().toString().split("\\s+");
                Toast.makeText(HomeActivity.this, "" + para.length, Toast.LENGTH_LONG).show();
            }
        });
    }
//NEED TO FIX THIS
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else {
            backToast = Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    //THIS IS END OF FIX AREA
}
