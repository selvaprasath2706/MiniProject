package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AuthoritySignUp extends AppCompatActivity {
    EditText E1,E2,E3,E4,E5;
    String userName,Password,confirmPassword,Department,Designation,Name,Area;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference,databaseReference2;
    Spinner spinner,spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_sign_up);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference("App");
        databaseReference2 = database.getReference("App");
        E1=findViewById(R.id.userName);
        E2=findViewById(R.id.Password);
        E3=findViewById(R.id.confirmPassword);
        E4=findViewById(R.id.designation);
        E5=findViewById(R.id.NameofPerson);
        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);
        List<String> l1=new ArrayList<>();
        l1.add("Electricity");
        l1.add("Water");
        l1.add("Tree");
        ArrayAdapter<String>adapter=new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,l1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Department = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> l2=new ArrayList<>();
        l2.add("Aranthangi");
        l2.add("Ramapuram");
        ArrayAdapter<String> adapter2=new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,l2);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Area = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void StaffSignUp(View view) {
        userName=E1.getText().toString();
        Password=E2.getText().toString();
        confirmPassword=E3.getText().toString();
        Designation=E4.getText().toString();
        Name=E5.getText().toString();

        if(!TextUtils.isEmpty(userName)){
            if(!TextUtils.isEmpty(Name)){
                if(!TextUtils.isEmpty(Password)){
                    if(!TextUtils.isEmpty(confirmPassword)){
                        if(!TextUtils.isEmpty(Designation)){
                            if(Password.equals(confirmPassword)){
                                mAuth.createUserWithEmailAndPassword(userName,Password)
                                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    //    Toast.makeText(SignUpPage.this, "Sucess", Toast.LENGTH_SHORT).show();
                                                    String uid = mAuth.getCurrentUser().getUid();

                                                    databaseReference2.child("Authorized").child(uid).child("Uid").setValue(uid);
                                                    databaseReference2.child("Authorized").child(uid).child("Name").setValue(Name);
                                                    databaseReference2.child("Authorized").child(uid).child("Department").setValue(Department);
                                                    databaseReference2.child("Authorized").child(uid).child("Area").setValue(Area);
                                                    databaseReference2.child("Authorized").child(uid).child("Designation").setValue(Designation);

                                                    databaseReference.child("User").child(Department).child(uid).child("Uid").setValue(uid);
                                                    databaseReference.child("User").child(Department).child(uid).child("Name").setValue(Name);
                                                    databaseReference.child("User").child(Department).child(uid).child("Department").setValue(Department);
                                                    databaseReference.child("User").child(Department).child(uid).child("Area").setValue(Area);
                                                    databaseReference.child("User").child(Department).child(uid).child("Designation").setValue(Designation);
                                                    Toast.makeText(AuthoritySignUp.this, "Sucessfully Signed Up", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(AuthoritySignUp.this,SignInPage.class);
                                                    startActivity(intent);
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Toast.makeText(AuthoritySignUp.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                        });

                            }
                            else{
                                Toast.makeText(this, "Both Passwords doesnt match", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(this, "Designation cannnot be Null", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(this, "Confirm Password cammot be null", Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(this, "Password cannot be Null", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Name cannnot be Null", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Username cannot be null", Toast.LENGTH_SHORT).show();
        }

    }
}