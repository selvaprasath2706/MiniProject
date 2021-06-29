package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetails extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    EditText E1,E2,E3;
    String Name,Email,MobNo;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        E1=findViewById(R.id.Nameoftheuser);
        E2=findViewById(R.id.EmailofUser);
        E3=findViewById(R.id.userMobno);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference("App");

    }

    public void SubmitDetails(View view) {
        Name=E1.getText().toString();
        Email=E2.getText().toString();
        MobNo=E3.getText().toString();

        if(!TextUtils.isEmpty(Name)){
            if(!TextUtils.isEmpty(Email)){
                if(!TextUtils.isEmpty(MobNo)){
                    String uid = mAuth.getCurrentUser().getUid();
                    databaseReference.child("User").child("People").child(uid).child("Name").setValue(Name);
                    databaseReference.child("User").child("People").child(uid).child("Email").setValue(Email);
                    databaseReference.child("User").child("People").child(uid).child("Mobno").setValue(MobNo);
                    databaseReference.child("User").child("People").child(uid).child("Completed").setValue("True");
                    Toast.makeText(this, "Details have been submitted Sucessfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UserDetails.this, UserHomePage.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(this, "Mobile No cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Email id cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }
}