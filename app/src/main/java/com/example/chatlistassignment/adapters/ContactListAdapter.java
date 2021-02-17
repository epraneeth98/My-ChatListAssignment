package com.example.chatlistassignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatlistassignment.R;
import com.example.chatlistassignment.activities.ContactDetailsActivity;
import com.example.chatlistassignment.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends PagedListAdapter<Contact, ContactListAdapter.ContactViewHolder> {
    Context mContext;

    public static DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ContactListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_data, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = getItem(position);
        if(contact==null)return;
        holder.textViewName.setText(contact.getName());
        holder.textViewNumber.setText(contact.getNumbers_list().get(0).first);
        Log.d("abc", "Second: " +
                ""+contact.getNumbers_list().get(0).second);
        Log.d("abc", contact.getNumbers_list().get(0).first);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> to_send = new ArrayList<>();
                for(Pair<String, String> p: contact.getNumbers_list()){
                    to_send.add(p.first+":"+p.second);
                }
                Intent myIntent = new Intent(mContext, ContactDetailsActivity.class);
                myIntent.putStringArrayListExtra("Contact_all_numbers", (ArrayList<String>) to_send);
                myIntent.putExtra("contact_name", contact.getName());
                Log.d("abc", "in ContactListAdapter: -->"+contact.getNumbers_list().size());
                mContext.startActivity(myIntent);
            }
        });

    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name_in_contact_row);
            textViewNumber = itemView.findViewById(R.id.number_in_contact_row);
        }
    }
}
