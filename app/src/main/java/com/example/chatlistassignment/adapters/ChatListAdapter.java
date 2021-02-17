package com.example.chatlistassignment.adapters;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.chatlistassignment.ItemClickListener;
import com.example.chatlistassignment.R;
import com.example.chatlistassignment.activities.EditUserInfoActivity;
import com.example.chatlistassignment.activities.FullScreenImageActivity;
import com.example.chatlistassignment.activities.MainActivity;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.utils.HelperFunctions;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;

import java.util.ArrayList;

import dagger.hilt.android.internal.managers.FragmentComponentManager;

public class ChatListAdapter extends PagedListAdapter<User, ChatListAdapter.ChatViewHolder> {

    Context context;
    ItemClickListener itemClickListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public ArrayList<User> selected_usersList = new ArrayList<>();
    FragmentViewModel fragmentViewModel;

    private static DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(User oldUser, User newUser) {
            return oldUser.getUid() == newUser.getUid();
        }

        @Override
        public boolean areContentsTheSame(User oldUser,
                                          User newUser) {
            return oldUser.equals(newUser);
        }
    };

    public ChatListAdapter(Context context, ItemClickListener itemClickListener, FragmentViewModel fragmentViewModel) {
        super(DIFF_CALLBACK);
        this.fragmentViewModel = fragmentViewModel;
        this.context = context;
        this.itemClickListener = itemClickListener;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chat_data, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        User user = getItem(position);
        Log.d("abc", "In OnBindViewHolder in adapter: " + user.getName());
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(getItem(position).getUid()));
        viewBinderHelper.closeLayout(String.valueOf(getItem(position).getUid()));
        holder.bindData(getItem(position));
        holder.textViewName.setText(user.getName());
        holder.textViewNumber.setText(user.getContactNumbers().get(0));
        holder.rowHeader.setText(HelperFunctions.getHeaderText(user.getLastUpdatedAt()));
        holder.rowHeader.setVisibility(View.GONE);
        if (position > 0) {
            User user2 = getItem(position - 1);
            if (!HelperFunctions.getHeaderText(user.getLastUpdatedAt()).equals(HelperFunctions.getHeaderText((user2.getLastUpdatedAt())))) {
                holder.rowHeader.setVisibility(View.VISIBLE);
            } else {
                holder.rowHeader.setVisibility(View.GONE);
            }
        } else {
            holder.rowHeader.setVisibility(View.VISIBLE);
        }

        if (user.getProfilePic() == null) {
            holder.imageViewProfilePic.setImageResource(R.drawable.ic_baseline_person_24);
            Glide.with(context)
                    .load(R.drawable.ic_baseline_person_24)
                    .circleCrop()
                    .into(holder.imageViewProfilePic);
        } else {
            holder.imageViewProfilePic.setImageURI(Uri.parse(user.getProfilePic()));
            Glide.with(context)
                    .load(Uri.parse(user.getProfilePic()))
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .circleCrop()
                    .into(holder.imageViewProfilePic);
        }
        holder.imageViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("abc", "In onBindView Holder in adapter: Image clicked");
                if (getItem(position).getProfilePic() == null) {
                    Toast.makeText(context, "No image to view", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("imageUri", getItem(position).getProfilePic());
                    Log.d("abc", "profile pic path: " + getItem(position).getProfilePic());
                    context.startActivity(intent);
                }
            }
        });
        holder.mainLayout.setOnClickListener(v -> {
            Log.d("abc", "cclliekd");
            Intent intentEditUserInfoActivity = new Intent(context, EditUserInfoActivity.class);
            intentEditUserInfoActivity.putExtra("User", getItem(position));
            context.startActivity(intentEditUserInfoActivity);
        });
        holder.txtEdit.setOnClickListener(v -> {
            Intent intentEditUserInfoActivity = new Intent(context, EditUserInfoActivity.class);
            intentEditUserInfoActivity.putExtra("User", getItem(position));
            context.startActivity(intentEditUserInfoActivity);
            Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();
            Log.d("abc", "edit clicked");

        });
        holder.txtDelete.setOnClickListener(v -> {
            Log.d("abc", "Finally hrere");
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                holder.swipeRevealLayout.close(true);
            };
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setPositiveButton(Html.fromHtml("<font color='#FF0000'>Delete</font>"), (dialog, which) -> {
                fragmentViewModel.deleteUser(getItem(position), context);
                notifyDataSetChanged();
            });
            ab.setMessage("Are you sure want to delete this contact?")
                    .setNegativeButton("No", dialogClickListener).show();
            notifyDataSetChanged();
        });

        if (selected_usersList.contains(getItem(position)))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.sky_blue));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewProfilePic;
        TextView textViewName, textViewNumber, rowHeader;
        Button txtEdit, txtDelete;
        RelativeLayout mainLayout;
        FragmentViewModel fragmentViewModel;
        SwipeRevealLayout swipeRevealLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfilePic = itemView.findViewById(R.id.image_view_profile_pic);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewNumber = itemView.findViewById(R.id.edit_text_contact_number);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            mainLayout = itemView.findViewById(R.id.main_layout);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout);
            rowHeader = itemView.findViewById(R.id.row_header);
            fragmentViewModel = ViewModelProviders.of((FragmentActivity) FragmentComponentManager.findActivity(context)).get(FragmentViewModel.class);
            rowHeader.setVisibility(View.GONE);
        }

        void bindData(User user) {
            textViewName.setText(user.getName());
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClicked(view, getAdapterPosition());
        }
    }
}
