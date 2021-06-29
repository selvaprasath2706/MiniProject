package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RandomDetails extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    EditText E1,E2;
    String s1,s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_details);
        databaseReference = database.getReference("App");
        E1=findViewById(R.id.editText);
        E2=findViewById(R.id.editText2);
    }

    public void SavetoDb(View view) {
        s1=E1.getText().toString();
        s2=E2.getText().toString();
        databaseReference.child("AreaCode").child(s1).child("Area").setValue(s2);
        Toast.makeText(this, "Data entered", Toast.LENGTH_SHORT).show();

    }
}