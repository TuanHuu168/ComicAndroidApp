package com.example.a4tcomic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReadingPagerAdapter extends RecyclerView.Adapter<ReadingPagerAdapter.PageViewHolder> {

    private List<Integer> pageImages;

    public ReadingPagerAdapter(List<Integer> pageImages) {
        this.pageImages = pageImages;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        holder.imageView.setImageResource(pageImages.get(position));
    }

    @Override
    public int getItemCount() {
        return pageImages.size();
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
