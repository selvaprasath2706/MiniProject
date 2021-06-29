package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInPage extends AppCompatActivity {
    TextView T1,T2,T3;
    EditText E1,E2;
    private FirebaseAuth mAuth;
    String userName,Password,dept=" ";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        E1=findViewById(R.id.username);
        E2=findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference("App");

    }

    public void signInwith(View view) {
    userName=E1.getText().toString();
    Password=E2.getText().toString();
    if(!TextUtils.isEmpty(userName)){
        if(!TextUtils.isEmpty(Password)){
            mAuth.signInWithEmailAndPassword(userName,Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                E1.setText("");
                                E2.setText("");
                                String uid = mAuth.getCurrentUser().getUid();
                                //   Toast.makeText(SignInPage.this, uid, Toast.LENGTH_SHORT).show();
                                databaseReference.child("Authorized").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(uid)){
                                            dept=snapshot.child(uid).child("Department").getValue().toString();
                                            if(dept.equals("Electricity")){
                                                Intent intent2=new Intent(SignInPage.this,EbHomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent2);

                                            }
                                            else if(dept.equals("Water")){
                                                Intent intent3=new Intent(SignInPage.this,WaterHomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent3);

                                            }
                                            else if(dept.equals("Tree")){
                                                Intent intent4=new Intent(SignInPage.this,TreeHomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent4);

                                            }


                                        }else{
                                            Intent intent1=new Intent(SignInPage.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent1);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//                            Toast.makeText(SignInPage.this, dept, Toast.LENGTH_SHORT).show();

                            } else if(!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(SignInPage.this, "Password has less than 8 characters", Toast.LENGTH_SHORT).show();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    String errorCode =(e).getErrorCode();

                                    if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                        Toast.makeText(SignInPage.this, "No matching account found", Toast.LENGTH_SHORT).show();
                                    } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                                        Toast.makeText(SignInPage.this, "User account has been disabled", Toast.LENGTH_SHORT).show();
                                    } else {
                                            Toast.makeText(SignInPage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    } catch(FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(SignInPage.this, "User Exists with same Email id", Toast.LENGTH_SHORT).show();
                                }

                                catch(Exception e) {
                                    Toast.makeText(SignInPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    });

        }else{
            Toast.makeText(this, "Password cannot be null", Toast.LENGTH_SHORT).show();
        }
    }else{
        Toast.makeText(this, "User Name Cannot be Null", Toast.LENGTH_SHORT).show();
    }

    }

    public void SignUpPage(View view) {
        Intent intent=new Intent(this,SignUpPage.class);
        startActivity(intent);
    }

    public void forgetPassword(View view) {
        userName=E1.getText().toString();
        if(!TextUtils.isEmpty(userName)){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = userName;

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInPage.this, "Email sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Please Enter your Email Id to proceed", Toast.LENGTH_SHORT).show();
        }
    }
}