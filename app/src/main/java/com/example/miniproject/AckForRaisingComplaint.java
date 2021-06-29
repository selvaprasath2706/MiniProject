package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AckForRaisingComplaint extends AppCompatActivity {
    TextView T1;
    String Refno,Department;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ack_for_raising_complaint);
        T1=findViewById(R.id.textView);
        Refno=getIntent().getStringExtra("Refno");
        Department=getIntent().getStringExtra("Department");
        T1.setText("your complaint for "+Department+" is raised with Refno"+Refno);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,UserHomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}