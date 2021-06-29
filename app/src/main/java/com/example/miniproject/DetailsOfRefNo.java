package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class DetailsOfRefNo extends AppCompatActivity {
    String Date,PhotoString,AreaCode,Complaint,RefNo,Solved,Remarks;
    double Latitude,Longitude;
    TextView T1,T2,T3,T4,T5,T6,T7;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_ref_no);
        Date=getIntent().getStringExtra("Date");
        Remarks=getIntent().getStringExtra("Remarks");
        PhotoString=getIntent().getStringExtra("PhotoString");
        AreaCode=getIntent().getStringExtra("AreaCode");
        Complaint=getIntent().getStringExtra("Complaint");
        RefNo=getIntent().getStringExtra("RefNo");
        Solved=getIntent().getStringExtra("Solved");
        Bundle b = getIntent().getExtras();
        Latitude= b.getDouble("Latitude");
        Longitude= b.getDouble("Longitude");
        T1=findViewById(R.id.textView2);
        T2=findViewById(R.id.textView4);
        T3=findViewById(R.id.textView6);
        T4=findViewById(R.id.textView8);
        T5=findViewById(R.id.textView10);
        T6=findViewById(R.id.textView12);
        T7=findViewById(R.id.textView15);
        imageView=findViewById(R.id.imageView);
        T1.setText(RefNo);
        T2.setText(Date);
        T3.setText("Lattitude: "+Latitude+"Longitude: "+Longitude);
        T4.setText(Solved);
        T5.setText(AreaCode);
        T6.setText(Complaint);
        T7.setText(Remarks);
        setImage();

    }
    private void setImage() {
        Bitmap imageBitmap = null;
        try {
            imageBitmap = decodeFromFirebaseBase64(PhotoString);
            imageView.setImageBitmap(imageBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void gmap(View view) {
        Uri mapUri = Uri.parse("geo:0,0?q="+Latitude+","+Longitude+"");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}