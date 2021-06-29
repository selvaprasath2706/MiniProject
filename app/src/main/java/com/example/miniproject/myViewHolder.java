package com.example.miniproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myViewHolder extends RecyclerView.ViewHolder {
    TextView t1,t2,t3;
    View v;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        t1=itemView.findViewById(R.id.textView1);
        t2=itemView.findViewById(R.id.textView2);
        t3=itemView.findViewById(R.id.textView3);
        v=itemView;
    }
}
