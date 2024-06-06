package com.example.a4tcomic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ChapterViewHolder> {

    private List<String> chapters;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ChaptersAdapter(List<String> chapters) {
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        String chapter = chapters.get(position);
        holder.tvChapter.setText(chapter);
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public void updateChapters(List<String> newChapters) {
        this.chapters = newChapters;
        notifyDataSetChanged();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        Button tvChapter;

        public ChapterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvChapter = itemView.findViewById(R.id.tv_chapter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
