package com.master.networkmanagementsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    Button logout,qr,terminal,embtn,findbtn;
    FirebaseAuth mFirebaseAuth;
    TextView Humid,temp;
    DatabaseReference dref;
    String status;
    ImageButton dectvtbtn;
    private long backPressedTime;
    private Toast backToast;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    ProgressDialog dialog;


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
        findbtn = findViewById(R.id.find);
        dectvtbtn = findViewById(R.id.removeAccount);

        dialog = new ProgressDialog(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent logouted = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(logouted);
            }
        });

        dectvtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    dialog.setMessage("Deactivating...Please Wait!");
                    dialog.show();
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Account Deactivated",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(HomeActivity.this,RegisterActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Deactivation Not Successful",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(HomeActivity.this, QrActivity.class);
                startActivity(q);
            }
        });

        findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(HomeActivity.this, DeviceHomeActivity.class);
                startActivity(f);
            }
        });

        terminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("For this facility you have to 'Termux Application' on your device. \nInstalled already?")
                        .setPositiveButton("[Yes] \n Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent t = getPackageManager().getLaunchIntentForPackage("com.termux");//com.server.auditor.ssh.client
                        startActivity(t);
                    }
                }).setNegativeButton("[No] \n Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                        myWebLink.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.termux&hl=en"));
                        startActivity(myWebLink);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
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

        if (backPressedTime + 2700 > System.currentTimeMillis()){
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
