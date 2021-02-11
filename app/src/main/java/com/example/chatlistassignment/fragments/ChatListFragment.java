package com.example.chatlistassignment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatlistassignment.ItemClickListener;
import com.example.chatlistassignment.R;
import com.example.chatlistassignment.activities.EditUserInfoActivity;
import com.example.chatlistassignment.adapters.ChatListAdapter;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.utils.AlertDialogHelper;
import com.example.chatlistassignment.utils.RecyclerItemClickListener;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;

import java.util.ArrayList;
import java.util.List;


public class ChatListFragment extends Fragment implements ItemClickListener, AlertDialogHelper.AlertDialogListener {
    ActionMode mActionMode;
    RecyclerView recyclerViewChatList;
    LinearLayoutManager layoutManager;
    ChatListAdapter chatListAdapter;
    List<User> userArrayList;
    ArrayList<User> queryArrayList;
    ArrayList<User> multiselect_list = new ArrayList<>();
    Menu context_menu;
    boolean isMultiSelect = false;
    FragmentViewModel fragmentViewModel;
    AlertDialogHelper alertDialogHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewModel = ViewModelProviders.of(this).get(FragmentViewModel.class);
        userArrayList = new ArrayList<>();
        queryArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        observeForDbChanges();
        observeQueryString();
        init(view);

        recyclerViewChatList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewChatList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else {
                    Toast.makeText(getActivity(), "Details Page", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<>();
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
            }
        }));
        return view;
    }

    //Important
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        Menu menu;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            this.menu = menu;
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("", "Are you sure want to delete all the selected contacts(s)?", "DELETE", "CANCEL", 1, false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<>();
            refreshAdapter();
        }
    };

    private void observeForDbChanges() {
        fragmentViewModel.userList.observe(getViewLifecycleOwner(), new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
                userArrayList = users.snapshot();
                chatListAdapter.submitList(users);
            }
        });
    }

    private void observeQueryString() {
        fragmentViewModel.getQueryString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                Log.d("TAG", "Inside ChatListFragment: " + query);
                queryChatList(query);
            }
        });
    }

    private void queryChatList(String query) {
        query = "%" + query + "%";
        fragmentViewModel.queryInit(query);
        fragmentViewModel.queriedUserList.observe(this, new Observer<PagedList<User>>() {
                    @Override
                    public void onChanged(PagedList<User> users) {
                        chatListAdapter.submitList(users);
                    }
                }
        );
    }

    private void init(View view) {
        alertDialogHelper = new AlertDialogHelper(getContext(), ChatListFragment.this);
        recyclerViewChatList = view.findViewById(R.id.recyclerview_chat_list);
        layoutManager = new LinearLayoutManager(getContext());
        chatListAdapter = new ChatListAdapter(getContext(), this, fragmentViewModel );
        recyclerViewChatList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewChatList.setLayoutManager(layoutManager);
        recyclerViewChatList.setAdapter(chatListAdapter);
    }

    public void refreshAdapter() {
        chatListAdapter.selected_usersList = multiselect_list;
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(userArrayList.get(position))) {
                multiselect_list.remove(userArrayList.get(position));
                //recyclerViewAdapter.SelectedUsersListChanged(multiselect_list);
            } else {
                multiselect_list.add(userArrayList.get(position));
                //recyclerViewAdapter.SelectedUsersListChanged(multiselect_list);
            }
            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("0");
            refreshAdapter();

        }
    }

    @Override
    public void onItemClicked(View view, int position) {
        Log.d("abc", "Clicked !!");
        Intent intentEditUserInfoActivity2 = new Intent(getContext(), EditUserInfoActivity.class);
        intentEditUserInfoActivity2.putExtra("User", userArrayList.get(position));
        startActivity(intentEditUserInfoActivity2);
    }

    @Override
    public void onPositiveClick(int from) {
        if (from == 1) {
            if (multiselect_list.size() > 0) {
                userArrayList = multiselect_list;
                for (int i = 0; i < multiselect_list.size(); i++)
                    fragmentViewModel.deleteUser(userArrayList.get(i), getContext());
                chatListAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }
                Toast.makeText(getActivity(), "Delete Click", Toast.LENGTH_SHORT).show();
            }
        } else if (from == 2) {
            if (mActionMode != null) {
                mActionMode.finish();
            }
        }
    }

    @Override
    public void onNegativeClick(int from) {
        mActionModeCallback.onDestroyActionMode(mActionMode);
//        (getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAdapter();
    }


}