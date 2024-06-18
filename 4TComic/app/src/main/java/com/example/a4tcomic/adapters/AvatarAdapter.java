package com.example.a4tcomic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;

import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private List<Integer> avatarList;

    public void setData(List<Integer> avatarList) {
        this.avatarList = avatarList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        holder.img_avatar.setImageResource(avatarList.get(position));
    }

    @Override
    public int getItemCount() {
        if (avatarList != null) {
            return avatarList.size();
        }
        return 0;
    }

    public static class AvatarViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img_avatar;
        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar_item);
        }
    }
}
