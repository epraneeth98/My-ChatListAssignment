package com.example.chatlistassignment.fragments;

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
import com.example.chatlistassignment.activities.MainActivity;
import com.example.chatlistassignment.adapters.ChatListAdapter;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;
import com.example.chatlistassignment.zextras.AlertDialogHelper;
import com.example.chatlistassignment.zextras.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatListFragment extends Fragment implements ItemClickListener {
    ActionMode mActionMode;

    @BindView(R.id.recyclerview_chat_list)
    RecyclerView recyclerViewChatList;

    LinearLayoutManager layoutManager;
    ChatListAdapter chatListAdapter;
    List<User> userArrayList;
    ArrayList<User> queryArrayList;
    FragmentViewModel fragmentViewModel;

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
        init(view);
        observeForDbChanges();
        observeQueryString();
        observeRecyclerView();
        return view;
    }

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

    private void observeRecyclerView() {
        chatListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerViewChatList.scrollToPosition(positionStart);
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                chatListAdapter.notifyDataSetChanged();
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
        //alertDialogHelper = new AlertDialogHelper(getContext(), ChatListFragment.this);
        recyclerViewChatList = view.findViewById(R.id.recyclerview_chat_list);
        layoutManager = new LinearLayoutManager(getContext());
        chatListAdapter = new ChatListAdapter(getContext(), this, fragmentViewModel);
        recyclerViewChatList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewChatList.setLayoutManager(layoutManager);
        recyclerViewChatList.setAdapter(chatListAdapter);
    }

    @Override
    public void onItemClicked(View view, int position) {
        ((MainActivity) getActivity()).switchTodetailfragment(userArrayList.get(position));
    }
}