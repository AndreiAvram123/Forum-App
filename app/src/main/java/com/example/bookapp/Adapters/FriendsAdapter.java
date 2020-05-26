package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;
import com.example.bookapp.databinding.ItemFriendsBinding;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.User;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private ArrayList<User> friends;
    private MainActivityInterface mainActivityInterface;

    public FriendsAdapter(@NonNull MainActivityInterface activityInterface) {
            friends = new ArrayList<>();
        this.mainActivityInterface = activityInterface;
    }

    public void setData(@NonNull ArrayList<User> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFriendsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_friends, parent, false);
        //ViewHolder viewHolder = new View
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(friends.get(position));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemFriendsBinding binding;

        ViewHolder(@NonNull ItemFriendsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(User friend) {
            binding.setFriend(friend);
//            Glide.with(binding.getRoot())
//                  //  .load(friend.getProfilePictureURL())
//                    .into(binding.imageView2);
      //TODo
            //replace
            //      binding.getRoot().setOnClickListener(view->mainActivityInterface.startChat(friend));
        }
    }
}
