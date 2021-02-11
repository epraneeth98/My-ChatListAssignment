package com.example.chatlistassignment.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.chatlistassignment.R;
import com.example.chatlistassignment.adapters.ChatListAdapter;
import com.example.chatlistassignment.adapters.ContactListAdapter;
import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;

public class ContactListFragment extends Fragment {

    RecyclerView recyclerView;
    ContactListAdapter contactListAdapter;
    FragmentViewModel fragmentViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewModel = new ViewModelProvider(this).get(FragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        init(view);
        observeContactDB();
        observeQueryString();

        return view;
    }

    private void observeQueryString() {
        fragmentViewModel.getQueryString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                query = "%" + query + "%";
                fragmentViewModel.queryContactInit(query);
                fragmentViewModel.queryContactList.observe(getViewLifecycleOwner(), new Observer<PagedList<Contact>>() {
                    @Override
                    public void onChanged(PagedList<Contact> contacts) {
                        contactListAdapter.submitList(contacts);
                    }
                });
            }
        });
    }

    private void observeContactDB() {
        fragmentViewModel.contactList.observe(getViewLifecycleOwner(), new Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(PagedList<Contact> contacts) {
                contactListAdapter.submitList(contacts);
            }
        });
    }


    private void init(View view) {
        contactListAdapter = new ContactListAdapter();
        recyclerView = view.findViewById(R.id.recyclerview_contact_list);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}