package com.example.chatlistassignment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatlistassignment.R;
import com.example.chatlistassignment.model.Contact;

public class ContactListAdapter extends PagedListAdapter<Contact, ContactListAdapter.ContactViewHolder> {

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

    public ContactListAdapter() {
        super(DIFF_CALLBACK);
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
        holder.textViewNumber.setText(contact.getNumber().get(0));

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
