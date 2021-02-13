package com.example.chatlistassignment.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;

import com.example.chatlistassignment.fragments.ChatListFragment;
import com.example.chatlistassignment.fragments.ContactListFragment;
import com.example.chatlistassignment.fragments.DataEntryFragment;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1)
            return new DataEntryFragment();
        else if(position==0)
            return new ChatListFragment();
        else
            return new ContactListFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1)
            return "DATA ENTRY";
        else if(position==0)
            return "CHAT LIST";
        else
            return "Contacts";
    }

    private void observeContactsCount() {
//        fragmentViewModel.getContactsCount().observe(getViewLifecycleOwner(), new Observer<Integer>(){
//            @Override
//            public void onChanged(Integer integer) {
//                Log.d("abc", "In OhChanged in ContactListFragment: "+integer);
//                //contacts_count.setText("Total no. of Contacts: "+ integer);
//            }
//        });
    }
}
