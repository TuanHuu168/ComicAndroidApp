package com.example.a4tcomic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.models.Chapter;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private List<Chapter> listChapter;
    private Context context;

    public ChapterAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Chapter> listChapter) {
        this.listChapter = listChapter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = listChapter.get(position);
        if (chapter == null) {
            return;
        }
        String title_time = context.getString(R.string.update_time_title);
//        holder.img_chapter.setImageResource(chapter.getResourceId());
//        holder.tv_name_chapter.setText(chapter.getName());
//        holder.chapterTimeUpdate.setText(title_time + " " + chapter.getTimeUpdate());

    }

    @Override
    public int getItemCount() {
        if (listChapter != null) {
            return listChapter.size();
        }
        return 0;
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img_chapter;
        private final TextView tv_name_chapter;
        private final TextView chapterTimeUpdate;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            img_chapter = itemView.findViewById(R.id.img_chapter);
            tv_name_chapter = itemView.findViewById(R.id.tv_name_chapter);
            chapterTimeUpdate = itemView.findViewById(R.id.tv_time_update);
        }
    }
}
