package com.example.duraivel.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppCentralizedControl2 extends AppCompatActivity
{
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference("control");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String n= dataSnapshot.child("allow").getValue().toString();
                if(n.equals("1"))
                {
                    Intent i= new Intent(AppCentralizedControl2.this,IndexPage.class);
                    startActivity(i);
                    AppCentralizedControl2.this.finish();

                }
                else
                {
                   String a="0";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
