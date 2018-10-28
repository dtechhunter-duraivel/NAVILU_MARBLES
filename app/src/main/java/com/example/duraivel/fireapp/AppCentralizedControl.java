package com.example.duraivel.fireapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.ListView;





import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import java.util.ArrayList;
import java.util.HashMap;

public class AppCentralizedControl extends AppCompatActivity {
    private RequestQueue mQueue,mQueuea;
    String s;
    String pid;
    String size;
    TextView t1;
    String brand;
    ListView listView;
    ListViewAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button butt;
    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    Context context;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        database = FirebaseDatabase.getInstance();

        myRef= database.getReference("control");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String n= dataSnapshot.child("allow").getValue().toString();
                if(n.equals("1"))
                {
                    String a="0";
                }
                else
                {
                    Intent i= new Intent(AppCentralizedControl.this,AppCentralizedControl2.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        butt=(Button)findViewById(R.id.but1);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                onBackPressed();

            }
        });
        pd = new ProgressDialog(AppCentralizedControl.this);


        butt.setVisibility(View.GONE);
        pd.setTitle("Requesting");
        pd.setMessage("Fetching Data");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        mQueue = Volley.newRequestQueue(this);
        mQueuea = Volley.newRequestQueue(this);

        Intent i=getIntent();
        s= i.getStringExtra("val");
        pd.show();

        jsonParse(s);




    }

    //To Display the product details as list
    private void populateList(String pid,String prodname,String stock,String avsale, String sk) {
        list=new ArrayList<HashMap<String,String>>();
        HashMap<String,String> hashmap=new HashMap<String, String>();
        hashmap.put(FIRST_COLUMN, "PRODUCT ID");
        hashmap.put(SECOND_COLUMN, pid);
        list.add(hashmap);
        HashMap<String,String> hashmap2=new HashMap<String, String>();
        hashmap2.put(FIRST_COLUMN, "PRODUCT NAME");
        hashmap2.put(SECOND_COLUMN, prodname);

        list.add(hashmap2);

        HashMap<String,String> hashmap3=new HashMap<String, String>();
        hashmap3.put(FIRST_COLUMN, "STOCK ON HAND");
        hashmap3.put(SECOND_COLUMN, stock);

        list.add(hashmap3);

        HashMap<String,String> hashmap4=new HashMap<String, String>();
        hashmap4.put(FIRST_COLUMN, "AVAILABLE FOR SALE");
        hashmap4.put(SECOND_COLUMN, avsale);

        list.add(hashmap4);

        HashMap<String,String> hashmap5=new HashMap<String, String>();
        hashmap5.put(FIRST_COLUMN, "SIZE/COLOR");
        hashmap5.put(SECOND_COLUMN, size);

        list.add(hashmap5);
        HashMap<String,String> hashmap6=new HashMap<String, String>();
        hashmap6.put(FIRST_COLUMN, "BRAND");
        hashmap6.put(SECOND_COLUMN, brand);

        list.add(hashmap6);
        HashMap<String,String> hashmap7=new HashMap<String, String>();
        hashmap7.put(FIRST_COLUMN, "SKU");
        hashmap7.put(SECOND_COLUMN, sk);

        list.add(hashmap7);

        listView=(ListView)findViewById(R.id.listView1);

        if(!pid.isEmpty()) {
            butt.setVisibility(View.VISIBLE);
        }



    }

    //To Retrieve the Product Detail by SKU or QRCode
    private void jsonParse(String iid)
    {
        String url = "https://inventory.zoho.com/api/v1/items?authtoken=**********************&organization_id=176795420&"+iid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            if(jsonArray.length()==0)
                            {
                                butt.setVisibility(View.GONE);
                                pd.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AppCentralizedControl.this);
                                builder.setMessage("No Records were found!")
                                        .setCancelable(false)
                                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                finish();
                                                onBackPressed();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }



                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject productid= jsonArray.getJSONObject(i);

                                pid =productid.getString("item_id");
                                size =productid.getString("cf_size");
                                brand=productid.getString("cf_brand_n");
                                itemDetails(pid);

                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                pd.dismiss();
                butt.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(AppCentralizedControl.this);
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                finish();
                                onBackPressed();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();



            }
        });




        request.setRetryPolicy(new DefaultRetryPolicy(1500,
                1,
                1));

        mQueue.add(request);
    }



    //TO Retrive the Individual Product Detail by item_id


    private void itemDetails(final String itemid) {

        String urla = "https://inventory.zoho.com/api/v1/items/"+itemid+"?authtoken=**********************&organization_id=176795420";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urla, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responser) {
                        try {
                            JSONObject productdet= responser.getJSONObject("item");
                            String ipid =productdet.getString("item_id");
                            butt.setVisibility(View.VISIBLE);
                            pd.dismiss();
                            String ast=productdet.getString("available_stock");
                            String firstName = productdet.getString("name");
                            String asale = productdet.getString("available_for_sale_stock");
                            String psku = productdet.getString("sku");
                            JSONObject cf =productdet.getJSONObject("custom_field_hash");
                            size =cf.getString("cf_size");
                            brand=cf.getString("cf_brand_n");
                            populateList(ipid,firstName,ast,asale,psku);
                            adapter=new ListViewAdapter(AppCentralizedControl.this, list);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "No Records were Found!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                pd.dismiss();
                butt.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(AppCentralizedControl.this);
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                finish();
                                onBackPressed();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(1500,
                1,
                1));
        mQueuea.add(request);
    }






    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }

}
