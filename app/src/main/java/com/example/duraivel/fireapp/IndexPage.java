package com.example.duraivel.fireapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IndexPage extends AppCompatActivity {
CardView but,but1;
TextView tex;
TextView contact;
FirebaseDatabase database;
DatabaseReference myRef;
final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        contact =(TextView)findViewById(R.id.contact);
        contact.setText(Html.fromHtml("<u>"+"Contact Developer"+"</u>"));
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout);
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

       try {
           ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo info = cm.getActiveNetworkInfo();
           if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
               Toast.makeText(getApplicationContext(), "Your internet connection is slow may take long time to fetch data", Toast.LENGTH_SHORT).show();
           }
       }
       catch (Exception e)

       {
           String c="0"; //Dummy variable
       }


        tex=(TextView)findViewById(R.id.te) ;
        but =(CardView)findViewById(R.id.b1);
        but1 =(CardView)findViewById(R.id.b2);


        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                try
                {

                    Intent mainIntent = new Intent(IndexPage
                            .this,SearchType.class);
                    IndexPage.this.startActivity(mainIntent);

                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Sorry Something Went Wrong! Try Again ",Toast.LENGTH_SHORT).show();    }


            }
        });
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Toast.makeText(getApplicationContext(),"Scanning",Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(IndexPage
                            .this,QRScanner.class);
                    IndexPage.this.startActivity(mainIntent);

                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Sorry Something Went Wrong! Try Again",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}
