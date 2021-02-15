package com.example.chatlistassignment.adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.chatlistassignment.fragments.ChatListFragment;
import com.example.chatlistassignment.fragments.ContactListFragment;
import com.example.chatlistassignment.fragments.DataEntryFragment;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    String count_in_view_pager_adapter = "Contacts";
    FragmentViewModel fragmentViewModel;
    Context mContext;

    public ViewPagerAdapter(@NonNull FragmentManager fm, FragmentViewModel fragmentViewModel, Context context) {
        super(fm);
        this.fragmentViewModel = fragmentViewModel;
        mContext = context;
        //observeContactsCount();
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
            return count_in_view_pager_adapter;
    }

//    private void observeContactsCount() {
//        fragmentViewModel.getContactsCount().observe((LifecycleOwner) mContext, new Observer<Integer>(){
//            @Override
//            public void onChanged(Integer integer) {
//                Log.d("abc", "In OhChanged in ContactListFragment: "+integer);
//                count_in_view_pager_adapter = "Contacts:"+ integer;
//            }
//        });
//    }
}
