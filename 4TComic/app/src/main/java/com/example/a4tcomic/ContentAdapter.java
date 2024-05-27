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

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {
    private Context context;
    private List<ContentItem> contentList;

    public ContentAdapter(Context context, List<ContentItem> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @Override
    public int getItemViewType(int position) {
        return contentList.get(position).getType();
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ContentItem.TYPE_COMIC) {
            view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        } else if (viewType == ContentItem.TYPE_ACCOUNT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_recently_updated, parent, false);
        }

        return new ContentViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        ContentItem contentItem = contentList.get(position);
        holder.contentImage.setImageResource(contentItem.getImageResource());
        holder.contentTitle.setText(shortenTitle(contentItem.getTitle()));
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView contentImage;
        TextView contentTitle;

        public ContentViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if(viewType == ContentItem.TYPE_COMIC || viewType == ContentItem.TYPE_UPDATE){
                contentImage = itemView.findViewById(R.id.comic_image);
                contentTitle = itemView.findViewById(R.id.comic_title);
            }
            else{
                contentImage = itemView.findViewById(R.id.user_image);
                contentTitle = itemView.findViewById(R.id.user_title);
            }

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