package com.example.comicandroidapp.story_detail;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicandroidapp.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<String> genreList;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> genreList) {
        this.genreList = genreList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.tv_genre.setText(genreList.get(position));
    }

    @Override
    public int getItemCount() {
        if (genreList != null)
            return genreList.size();
        return 0;
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_genre;
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_genre = itemView.findViewById(R.id.tv_genre);
        }
    }

}
