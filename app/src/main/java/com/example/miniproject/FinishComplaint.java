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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinishComplaint extends AppCompatActivity {

    TextView T1,T2;
    EditText E1;
    double Latitude,Longitude;
    String RefNo,Remarks,PhotoString,AreaName,AreaCode,Department,Complaint,Date,Solved;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference,databaseReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_complaint);
        T1=findViewById(R.id.textView1);
        T2=findViewById(R.id.textView2);
        E1=findViewById(R.id.Remarks);
        RefNo=getIntent().getStringExtra("RefNo");
        Department=getIntent().getStringExtra("Department");
        AreaName=getIntent().getStringExtra("AreaName");
        databaseReference = database.getReference("App");
        databaseReference2 = database.getReference("App");
    //    Toast.makeText(this, Department+AreaName+RefNo, Toast.LENGTH_SHORT).show();
    }

    public void IssueCompleted(View view) {
        Remarks=E1.getText().toString();
        if(!TextUtils.isEmpty(Remarks)){
            databaseReference.child("ComplaintSearch").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(RefNo)){
                        Complaint=snapshot.child(RefNo).child("complaint").getValue().toString();
                        Solved=snapshot.child(RefNo).child("solved").getValue().toString();
                        //Department=snapshot.child(RefNo).child("department").getValue().toString();
                        Date=snapshot.child(RefNo).child("date").getValue().toString();
                        Latitude=Double.parseDouble(snapshot.child(RefNo).child("latitude").getValue().toString());
                        Longitude=Double.parseDouble(snapshot.child(RefNo).child("longitude").getValue().toString());
                        Solved=snapshot.child(RefNo).child("solved").getValue().toString();
                        AreaCode=snapshot.child(RefNo).child("areacode").getValue().toString();
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("refno").setValue(RefNo);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("photostring").setValue(PhotoString);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("latitude").setValue(Latitude);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("longitude").setValue(Longitude);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("complaint").setValue(Complaint);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("solved").setValue("True");
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("areacode").setValue(AreaCode);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("date").setValue(Date);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("department").setValue(Department);
                        databaseReference2.child("Complaint").child(Department).child(AreaName).child("Solved").child(RefNo).child("remarks").setValue(Remarks);
                        databaseReference2.child("ComplaintSearch").child(RefNo).child("solved").setValue("True");
                        databaseReference2.child("ComplaintSearch").child(RefNo).child("remarks").setValue(Remarks);
//                        Toast.makeText(FinishComplaint.this, AreaCode, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(FinishComplaint.this, "Ref No Unavailable", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FinishComplaint.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReference.child("Complaint").child(Department).child(AreaName).child("Unsolved").child(RefNo).removeValue();

           Toast.makeText(this, "The Issue Has been Resolved", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,UnSolvedProblems.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Area",AreaName);
            intent.putExtra("Department",Department);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Remarks Cannot be Empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,UnSolvedProblems.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Area",AreaName);
        intent.putExtra("Department",Department);
        startActivity(intent);
    }
}