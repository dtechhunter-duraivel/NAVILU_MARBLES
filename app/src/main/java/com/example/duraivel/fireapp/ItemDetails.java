package com.example.duraivel.fireapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemDetails extends AppCompatActivity {
    private RequestQueue mQueue,mQueuea;
    String s;
    String pid;
    String size;
    TextView t1;
    String brand;
    ListView listView;
    ListViewAdapter2 adapter;

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button butt;
    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    Context context;
    ProgressDialog pd;
    ArrayList<String> itemid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        itemid=new ArrayList<String>();
        butt=(Button)findViewById(R.id.but1);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                onBackPressed();

            }
        });
        pd = new ProgressDialog(ItemDetails.this);
        butt.setVisibility(View.GONE);
        pd.setTitle("Requesting");
        pd.setMessage("Fetching Data");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        mQueue = Volley.newRequestQueue(this);
        mQueuea = Volley.newRequestQueue(this);

//database = FirebaseDatabase.getInstance();
        //     myRef= database.getReference("Items");
        //
        Intent i=getIntent();
        s= i.getStringExtra("val");
        pd.show();
        list=new ArrayList<HashMap<String,String>>();
        jsonParse(s);
        listView=(ListView)findViewById(R.id.listView1);
        adapter=new ListViewAdapter2(ItemDetails.this, list);
        listView.setAdapter(adapter);


      listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

      {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
          {

             String a=itemid.get(position);
             Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();
              Intent i =new Intent(ItemDetails.this,ItemsList.class);
              i.putExtra( "val",a);
              startActivity(i);
              overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

          }
      });

    }


    //To Display the product details as list
    private void populateList(String pid,String prodname,String stock,String avsale, String sk) {
        // TODO Auto-generated method stub


    HashMap<String, String> hashmap = new HashMap<String, String>();
    hashmap.put(FIRST_COLUMN, prodname);
    hashmap.put(SECOND_COLUMN, "STOCK ON HAND"+" "+sk);
    list.add(hashmap);
    itemid.add(pid);

    if(!pid.isEmpty()) {
            butt.setVisibility(View.VISIBLE);
        }



    }


    //To Retrieve the Product Detail by SKU or QRCode
    private void jsonParse(String iid) {

        String url = "https://inventory.zoho.com/api/v1/items?authtoken=**********************&organization_id=176795420&"+iid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            //  JSONObject jsonArray = response.getJSONObject("item");
                           int len=jsonArray.length();
                            if(jsonArray.length()==0)
                            {
                                butt.setVisibility(View.GONE);
                                pd.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetails.this);
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

if(len > 20)
{
    pd.dismiss();
    for (int k = 0; k < 19; k++)
    {
        JSONObject productid= jsonArray.getJSONObject(k);
        pid =productid.getString("item_id");
        String  nam =productid.getString("name");
        size =productid.getString("cf_size");
        String soh=productid.getString("stock_on_hand");
        populateList(pid,nam,size,brand,soh);

    }

}
else
{
    pd.dismiss();
    for (int j = 0; j < jsonArray.length(); j++)
    {
        JSONObject productid= jsonArray.getJSONObject(j);
        pid =productid.getString("item_id");
        String  nam =productid.getString("name");
        size =productid.getString("cf_size");
        String soh=productid.getString("stock_on_hand");
        populateList(pid,nam,size,brand,soh);

    }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetails.this);
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
                            // JSONArray jsonArray = responser.getJSONArray("items");
                            JSONObject productdet= responser.getJSONObject("item");
                            String ipid =productdet.getString("item_id");
                            butt.setVisibility(View.VISIBLE);
                            pd.dismiss();
                            String ast=productdet.getString("available_stock");
                            String firstName = productdet.getString("name");
                            String asale = productdet.getString("available_for_sale_stock");
                            int psku = 2;
                            JSONObject cf =productdet.getJSONObject("custom_field_hash");
                            size =cf.getString("cf_size");
                            brand=cf.getString("cf_brand_n");



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
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetails.this);
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
