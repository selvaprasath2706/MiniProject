package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {
    EditText E1,E2,E3;
    String userName,Password,confirmPassword,uid;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        E1=findViewById(R.id.signupusername);
        E2=findViewById(R.id.signupPassword);
        E3=findViewById(R.id.signupConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference("App");
    }

    public void Signup(View view) {
        userName=E1.getText().toString();
        Password=E2.getText().toString();
        confirmPassword=E3.getText().toString();
        if(!TextUtils.isEmpty(userName)){
            if(!TextUtils.isEmpty(Password)){
                if(!TextUtils.isEmpty(confirmPassword)){
                    if(Password.equals(confirmPassword)){
                        mAuth.createUserWithEmailAndPassword(userName,Password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            //    Toast.makeText(SignUpPage.this, "Sucess", Toast.LENGTH_SHORT).show();
                                            String uid = mAuth.getCurrentUser().getUid();

                                            databaseReference.child("User").child("People").child(uid).child("Uid").setValue(uid);
                                            databaseReference.child("User").child("People").child(uid).child("Completed").setValue("False");
                                            E1.setText("");
                                            E2.setText("");
                                            E3.setText("");
                                            Toast.makeText(SignUpPage.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(SignUpPage.this,UserDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                        else if(!task.isSuccessful()) {
                                            try {
                                                throw task.getException();
                                            } catch(FirebaseAuthWeakPasswordException e) {
                                                Toast.makeText(SignUpPage.this, "Password has less than 8 characters", Toast.LENGTH_SHORT).show();
                                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                                Toast.makeText(SignUpPage.this, "Invalid email Id", Toast.LENGTH_SHORT).show();
                                            } catch(FirebaseAuthUserCollisionException e) {
                                                Toast.makeText(SignUpPage.this, "User Exists with same Email id", Toast.LENGTH_SHORT).show();
                                            } catch(Exception e) {
                                                Toast.makeText(SignUpPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                            // If sign in fails, display a message to the user.

                                        }
                                    }


                                });

                    }
                    else{
                        Toast.makeText(this, "Ensure Both passwords are the same", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "Confirm Password Cannot be Null", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Password field cannot be null", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "User Name is empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void SignInPage(View view) {
        Intent intent1=new Intent(SignUpPage.this,SignInPage.class);
        startActivity(intent1);
    }
}