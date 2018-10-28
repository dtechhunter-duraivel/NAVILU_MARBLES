package com.example.duraivel.fireapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter2 extends BaseAdapter
{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";

    public ListViewAdapter2(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder{
        TextView txtFirst;
        TextView txtSecond;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ListViewAdapter2.ViewHolder holder;

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.list_view, null);
            holder=new ListViewAdapter2.ViewHolder();

            holder.txtFirst=(TextView) convertView.findViewById(R.id.TextFirst);
            holder.txtSecond=(TextView) convertView.findViewById(R.id.TextSecond);


            convertView.setTag(holder);
        }else{

            holder=(ListViewAdapter2.ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map=list.get(position);
        holder.txtFirst.setText(map.get(FIRST_COLUMN));
        holder.txtSecond.setText(map.get(SECOND_COLUMN));


        return convertView;
    }

}
