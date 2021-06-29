package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EbHomePage extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eb_home_page);
        databaseReference = database.getReference("App");
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.officials_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.seachComplaint:
                Intent i2 = new Intent(EbHomePage.this, SearchComplaint.class);
                startActivity(i2);
                break;


            case R.id.updatePassword:
                Intent i3 = new Intent(EbHomePage.this, UpdatePassword.class);
                startActivity(i3);
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent i4 = new Intent(EbHomePage.this, SignInPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void MovetoUnsolvedCases(View view) {
        String uid = mAuth.getCurrentUser().getUid();
        databaseReference.child("Authorized").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uid)) {
                    String Area = snapshot.child(uid).child("Area").getValue().toString();
                    Intent intent=new Intent(EbHomePage.this,UnSolvedProblems.class);
                    intent.putExtra("Department","Electricity");
                    intent.putExtra("Area",Area);
                    startActivity(intent);

                }else{
                    Toast.makeText(EbHomePage.this, "You haven't got proper rights to do this", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void MoveToSolvedCases(View view) {
        String uid = mAuth.getCurrentUser().getUid();
        databaseReference.child("Authorized").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uid)) {
                    String Area = snapshot.child(uid).child("Area").getValue().toString();
                    Intent intent=new Intent(EbHomePage.this,SolvedCases.class);
                    intent.putExtra("Department","Electricity");
                    intent.putExtra("Area",Area);
                    startActivity(intent);

                }else{
                    Toast.makeText(EbHomePage.this, "You haven't got proper rights to do this", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

//date,location