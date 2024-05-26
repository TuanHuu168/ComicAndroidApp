package com.example.a4tcomic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {
    private Context context;
    private List<ComicItem> comicList;

    public ComicAdapter(Context context, List<ComicItem> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        ComicItem comicItem = comicList.get(position);
        holder.comicImage.setImageResource(comicItem.getImageResource());
        holder.comicTitle.setText(shortenTitle(comicItem.getTitle()));
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView comicImage;
        TextView comicTitle;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            comicImage = itemView.findViewById(R.id.comic_image);
            comicTitle = itemView.findViewById(R.id.comic_title);
        }
    }

    private String shortenTitle(String title) {
        if (title.length() > 12) {
            return title.substring(0, 12) + "...";
        } else {
            return title;
        }
    }
}
