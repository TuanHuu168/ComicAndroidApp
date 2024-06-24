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

import com.bumptech.glide.Glide;
import com.example.a4tcomic.R;
import com.example.a4tcomic.activities.story.StoryDetailActivity;
import com.example.a4tcomic.models.Comic;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {
    private Context context;
    private List<Comic> comicList;

    public ContentAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Comic comic = comicList.get(position);
        Glide.with(context)
                .load(comic.getImg_url()) // Tải hình ảnh từ URL
                .placeholder(R.drawable.truyen1) // Hình ảnh thay thế khi tải
                .into(holder.contentImage);
        holder.contentTitle.setText(shortenTitle(comic.getTitle()));

        // Nhấn vào sẽ mở trang chi tiết
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện nhấn vào đây
                Intent intent = new Intent(context, StoryDetailActivity.class);
                intent.putExtra("comic_id", comic.getId()); // Truyền thêm ID của truyện để hiển thị chi tiết
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView contentImage;
        TextView contentTitle;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            contentImage = itemView.findViewById(R.id.imgComic);
            contentTitle = itemView.findViewById(R.id.lblComic);
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
