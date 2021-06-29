package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserHomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();
        databaseReference = database.getReference("App");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myaccount:
                String uid = mAuth.getCurrentUser().getUid();
                Intent i1 = new Intent(UserHomePage.this,AccountDetails.class);
                i1.putExtra("uid",uid);
                startActivity(i1);
                break;
            case R.id.seachComplaint:
                Intent i2 = new Intent(UserHomePage.this, SearchComplaint.class);
                startActivity(i2);
                break;

            case R.id.complaintsRaised:
                Intent i3=new Intent(UserHomePage.this,ComplaintsRaisedByUser.class);
                String Uid = mAuth.getCurrentUser().getUid();
                i3.putExtra("uid",Uid);
                startActivity(i3);
                break;

            case R.id.updatePassword:
                Intent i5 = new Intent(UserHomePage.this, UpdatePassword.class);
                startActivity(i5);
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent i4 = new Intent(UserHomePage.this, SignInPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(context, "Without Location Services This app cannot work..", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }, 3000);   //3 seconds
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null)
        {

            Intent i=new Intent(this,SignInPage.class);
            startActivity(i);
        }
        checkPermission();

    }

    public void SwitchToEb(View view) {
        Intent intent1=new Intent(this, RaiseAnIssue.class);
        intent1.putExtra("dept","Electricity");
        startActivity(intent1);
    }


    public void SwitchToWater(View view) {
        Intent intent1=new Intent(this, RaiseAnIssue.class);
        intent1.putExtra("dept","Water");
        startActivity(intent1);
    }

    public void SwitchToTree(View view) {
        Intent intent1=new Intent(this, RaiseAnIssue.class);
        intent1.putExtra("dept","Tree");
        startActivity(intent1);
    }
}