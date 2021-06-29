package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountDetails extends AppCompatActivity {
    String uid,Name,Emailid,MobileNo;
    TextView T1,T2,T3;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        uid=getIntent().getStringExtra("uid");
        T1=findViewById(R.id.EmailId);
        T2=findViewById(R.id.name);
        T3=findViewById(R.id.Mobileno);
        databaseReference = database.getReference("App");
        databaseReference.child("User").child("People").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uid)){
                    Emailid=snapshot.child(uid).child("Email").getValue().toString();
                    Name=snapshot.child(uid).child("Name").getValue().toString();
                    MobileNo=snapshot.child(uid).child("Mobno").getValue().toString();
                    T1.setText(Emailid);
                    T2.setText(Name);
                    T3.setText(MobileNo);
                }
                else {
                    T1.setText(" ");
                    T2.setText(" ");
                    T3.setText(" ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}