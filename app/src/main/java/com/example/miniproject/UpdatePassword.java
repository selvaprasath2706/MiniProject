package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {
    String  password,retypepassword;
    EditText E1,E2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        E1=findViewById(R.id.editTextTextPassword);
        E2=findViewById(R.id.editTextTextPassword2);
        mAuth = FirebaseAuth.getInstance();
    }

    public void updatePassword(View view) {
        password=E1.getText().toString();
        retypepassword=E2.getText().toString();
        if(!TextUtils.isEmpty(password)){
            if(!TextUtils.isEmpty(retypepassword)){
                if(password.equals(retypepassword)){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = password;
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UpdatePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(this, "Password And Confirm Passsword DOesn't Match", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Retype Password cannot be empty", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
        }



    }
}