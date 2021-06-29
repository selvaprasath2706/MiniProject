package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String dept="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference("App");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                routeToRespectivePage();
            }
        }, 1000);

    }

    private void routeToRespectivePage() {

        if (mAuth.getCurrentUser() == null) {
            Intent i = new Intent(HomePage.this,SignInPage.class);
            startActivity(i);
        }else{

            String uid = mAuth.getCurrentUser().getUid();
            databaseReference.child("Authorized").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(uid)){
                        dept=snapshot.child(uid).child("Department").getValue().toString();
                        if(dept.equals("Electricity")){
                            Intent intent2=new Intent(HomePage.this,EbHomePage.class);
                            startActivity(intent2);
                        //    Toast.makeText(HomePage.this, dept, Toast.LENGTH_SHORT).show();

                        }
                        else if(dept.equals("Water")){
                            Intent intent3=new Intent(HomePage.this,WaterHomePage.class);
                            startActivity(intent3);

                        }
                        else if(dept.equals("Tree")){
                            Intent intent4=new Intent(HomePage.this,TreeHomePage.class);
                            startActivity(intent4);

                        }

                    }else{
                        Intent intent1=new Intent(HomePage.this, UserHomePage.class);
                        startActivity(intent1);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }

           }

    @Override
    protected void onStart() {
        super.onStart();

       //mAuth.signOut();
    }
}