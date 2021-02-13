package com.example.chatlistassignment.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatlistassignment.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Pair<String,String>> mTypeNumberList;

    public ListViewAdapter(Context mContext, ArrayList<Pair<String, String>> mTypeNumberList) {
        this.mContext = mContext;
        this.mTypeNumberList = mTypeNumberList;
    }

    @Override
    public int getCount() {
        return mTypeNumberList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTypeNumberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_contact_details_data, parent, false);
        TextView numberType = row.findViewById(R.id.number_type_in_contact_details);
        TextView respectiveNumber = row.findViewById(R.id.number_in_contact_details);

        numberType.setText(mTypeNumberList.get(position).second);
        respectiveNumber.setText("      "+mTypeNumberList.get(position).first);
        return row;
    }
}
