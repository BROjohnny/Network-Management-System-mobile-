package com.master.networkmanagementsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button qr,terminal,embtn,findbtn;
    TextView Humid,temp;
    DatabaseReference dref;
    String status;
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private long backPressedTime;
    private Toast backToast;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        qr = findViewById(R.id.qr);
        terminal = findViewById(R.id.terminal);
        embtn = findViewById(R.id.embtn);
        Humid = findViewById(R.id.humid);
        temp = findViewById(R.id.tempdata);
        findbtn = findViewById(R.id.find);
        toolBar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        dialog = new ProgressDialog(this);


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

//        side pannel start
        setSupportActionBar(toolBar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolBar,
                R.string.openNav,
                R.string.closeNav);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_deactive:
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
                break;
            case R.id.nav_logout:
                finish();
                FirebaseAuth.getInstance().signOut();
                Intent logouted = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(logouted);
                break;
        }
        return true;
    }
}
