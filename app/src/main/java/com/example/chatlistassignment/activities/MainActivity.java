package com.example.chatlistassignment.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.chatlistassignment.R;
import com.example.chatlistassignment.adapters.ViewPagerAdapter;
import com.example.chatlistassignment.fragments.ContactListFragment;
import com.example.chatlistassignment.fragments.DataEntryFragment;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.utils.ContactsChangeListener;
import com.example.chatlistassignment.utils.IChangeListener;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;

    FragmentViewModel fragmentViewModel;
    private int READ_CONTACT_REQUEST_CODE = 100;

    IChangeListener iChangeListener = new IChangeListener() {
        @Override
        public void onContactsChanged() {
            fragmentViewModel.completeContactSync();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentViewModel, this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        fragmentViewModel = new ViewModelProvider(this).get(FragmentViewModel.class);

        checkPermissionSyncContact();
        observeContactsCountOnViewPager();
    }

    private void observeContactsCountOnViewPager() {
        fragmentViewModel.getContactsCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("abc", "In OhChanged in ContactListFragment: " + integer);
                //viewPager.setText("Total no. of Contacts: "+ integer);
            }
        });
    }

    private void checkPermissionSyncContact() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_REQUEST_CODE);
        else
            fragmentViewModel.completeContactSync();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContactsChangeListener.getInstance(this).startContactsObservation(iChangeListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACT_REQUEST_CODE) {
            if (grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
                fragmentViewModel.completeContactSync();
            } else {
                Toast.makeText(this, "Contact Sync failed. Please grant contacts permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                FragmentViewModel.setQueryString(query);
                Log.d("TAG", "Inside onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                FragmentViewModel.setQueryString(newText);
                Log.d("TAG", "Inside onQueryTextChange: " + newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContactsChangeListener.getInstance(this).stopContactsObservation();
    }

    public void switchTodetailfragment(User user) {
        LinearLayout relativeLayout = findViewById(R.id.chatlist_fragment);
        relativeLayout.setVisibility(View.GONE);

        Log.d("abc", "in mainActivity, switch to detail fragment");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.chatlist_fragment, DataEntryFragment.newInstance(user))
                .addToBackStack("tag")
                .commit();
    }
}