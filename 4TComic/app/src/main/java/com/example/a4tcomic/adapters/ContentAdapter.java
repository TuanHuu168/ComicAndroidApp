package com.example.a4tcomic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.activities.story.StoryDetailActivity;
import com.example.a4tcomic.models.ContentItem;

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

        // nhấn vào sẽ mở trang trang chi tiết
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // xử lý sự kiện nhấn vào đây
                Intent intent = new Intent(context, StoryDetailActivity.class);
                context.startActivity(intent);
            }
        });
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
            if(viewType == ContentItem.TYPE_COMIC){
                contentImage = itemView.findViewById(R.id.imgComic);
                contentTitle = itemView.findViewById(R.id.lblComic);
            }
            else if (viewType == ContentItem.TYPE_UPDATE){
                contentImage = itemView.findViewById(R.id.imgRecentlyUpdated);
                contentTitle = itemView.findViewById(R.id.lblRecentlyUpdated);
            }
            else{
                contentImage = itemView.findViewById(R.id.imgUser);
                contentTitle = itemView.findViewById(R.id.lblUser);
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