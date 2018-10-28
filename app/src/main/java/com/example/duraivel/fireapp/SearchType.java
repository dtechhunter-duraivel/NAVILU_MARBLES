package com.example.duraivel.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchType extends AppCompatActivity {
Button search,inc;
EditText e1;
    String pbrand;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String productId;
    Switch searchMode;
String smode="search";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        search = (Button)findViewById(R.id.but1);
        e1= (EditText)findViewById(R.id.e1);//Brand Name Input Field
searchMode=(Switch)findViewById(R.id.switchbut);

       searchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(searchMode.isChecked())
                {
                    smode="brand";
                    e1.setHint("ENTER THE BRAND NAME");
                    Toast.makeText(getApplicationContext(),"Product Name,SKU,Description",Toast.LENGTH_LONG).show();

                }
                else
                {
                    smode="search";
                    e1.setHint("ENTER THE SEARCH KEYWORD");
                }
            }
        });



    search.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
if(smode.equals("brand"))
{
    try

    {
        pbrand = e1.getText().toString().trim();
        if (!pbrand.isEmpty()) {
            Intent i = new Intent(SearchType.this, ItemDetails.class);
            i.putExtra("val", "cf_brand_n=" + pbrand);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please fill out the Field ", Toast.LENGTH_LONG).show();

        }
    }
    catch (Exception e)
    {
        Toast.makeText(getApplicationContext(),"Sorry Something Went Wrong!",Toast.LENGTH_LONG).show();
    }

}
else if(smode.equals("search"))
{
    try

    {
        pbrand = e1.getText().toString().trim();
        if (!pbrand.isEmpty()) {
            Intent i = new Intent(SearchType.this, ItemDetails.class);
            i.putExtra("val", "search_text=" + pbrand);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please fill out the Field ", Toast.LENGTH_LONG).show();

        }
    }
    catch (Exception e)
    {
        Toast.makeText(getApplicationContext(),"Sorry Something Went Wrong!",Toast.LENGTH_LONG).show();
    }

}

        }
    });


    }
}
