package com.example.chatlistassignment.adapters;

import android.app.AlertDialog;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.chatlistassignment.ItemClickListener;
import com.example.chatlistassignment.R;
import com.example.chatlistassignment.activities.EditUserInfoActivity;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    public ArrayList<User> userArrayList;
    ItemClickListener itemClickListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public ArrayList<User> selected_usersList = new ArrayList<>();

    public RecyclerViewAdapter(Context context, ArrayList<User> userArrayList, ItemClickListener itemClickListener) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.itemClickListener = itemClickListener;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chat_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(userArrayList.get(position).getUid()));
        viewBinderHelper.closeLayout(String.valueOf(userArrayList.get(position).getUid()));
        holder.bindData(userArrayList.get(position));

        User user = userArrayList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewNumber.setText(user.getContactNumber());

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

        if (selected_usersList.contains(userArrayList.get(position)))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.sky_blue));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
    }

    @Override
    public int getItemCount() {
        return userArrayList == null ? 0 : userArrayList.size();
    }

    public void updateData(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewProfilePic;
        TextView textViewName, textViewNumber;

        Button txtEdit, txtDelete;
        RelativeLayout mainLayout;
        FragmentViewModel fragmentViewModel;
        SwipeRevealLayout swipeRevealLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProfilePic = itemView.findViewById(R.id.image_view_profile_pic);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewNumber = itemView.findViewById(R.id.text_view_number);

            txtDelete = itemView.findViewById(R.id.txtDelete);
            mainLayout = itemView.findViewById(R.id.main_layout);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout);
            fragmentViewModel = ViewModelProviders.of((FragmentActivity) context).get(FragmentViewModel.class);
            mainLayout.setOnClickListener(v -> {
                Log.d("abc", "cclliekd");
                Intent intentEditUserInfoActivity = new Intent(context, EditUserInfoActivity.class);
                intentEditUserInfoActivity.putExtra("User", userArrayList.get(getAdapterPosition()));
                context.startActivity(intentEditUserInfoActivity);
            });

            txtEdit.setOnClickListener(v -> {
                Intent intentEditUserInfoActivity = new Intent(context, EditUserInfoActivity.class);
                intentEditUserInfoActivity.putExtra("User", userArrayList.get(getAdapterPosition()));
                context.startActivity(intentEditUserInfoActivity);
                Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();
                Log.d("abc", "edit clicked");
            });
            txtDelete.setOnClickListener(v -> {
                Log.d("abc", "Finally hrere");
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        default:
                            swipeRevealLayout.close(true);
                    }
                };

                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setPositiveButton(Html.fromHtml("<font color='#FF0000'>Delete</font>"), (dialog, which) -> fragmentViewModel.deleteUser(userArrayList.get(getAdapterPosition()), context));
                ab.setMessage("Are you sure want to delete this contact? \n\nNote: Use long press to delete multiple contacts.")
                        .setNegativeButton("No", dialogClickListener).show();

            });
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
