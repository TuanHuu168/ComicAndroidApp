package com.example.a4tcomic.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.app_interface.IClickChapter;
import com.example.a4tcomic.models.Chapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private List<Chapter> listChapter;
    private final IClickChapter iClickChapter;

    public ChapterAdapter(IClickChapter iClickChapter) {
        this.iClickChapter = iClickChapter;
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

        if (!chapter.getImg_url().equals(""))
            Picasso.get().load(chapter.getImg_url()).into(holder.img_chapter);

        holder.tv_name_chapter.setText(chapter.getOrder() + ": " + chapter.getTitle());

        if (chapter.getCreated_at() > 0)
            holder.chapterTimeUpdate.setText("" + convertTime(chapter.getCreated_at()));

        holder.cardView.setOnClickListener(v -> {
            iClickChapter.onClickChapter(chapter.getPdf_url()+"");
        });
    }

    public String convertTime(Long time) {
        String date = "";
        String year = String.valueOf(time).substring(0, 4);
        String month = String.valueOf(time).substring(4, 6);
        String day = String.valueOf(time).substring(6, 8);
        date = day + "/" + month + "/" + year;
        return date;
    }

    @Override
    public int getItemCount() {
        if (listChapter != null) {
            return listChapter.size();
        }
        return 0;
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_chapter;
        private TextView tv_name_chapter;
        private TextView chapterTimeUpdate;
        private CardView cardView;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_chapter);
            img_chapter = itemView.findViewById(R.id.img_chapter);
            tv_name_chapter = itemView.findViewById(R.id.tv_name_chapter);
            chapterTimeUpdate = itemView.findViewById(R.id.tv_time_update);
        }
    }
}
