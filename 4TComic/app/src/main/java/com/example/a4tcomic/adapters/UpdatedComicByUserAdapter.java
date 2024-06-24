package com.example.a4tcomic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a4tcomic.R;
import com.example.a4tcomic.db.AuthorsDB;
import com.example.a4tcomic.models.Author;
import com.example.a4tcomic.models.Comic;

import java.util.List;

public class UpdatedComicByUserAdapter extends RecyclerView.Adapter<UpdatedComicByUserAdapter.UpdatedComicViewHolder> {
    private Context context;
    private List<Comic> comicList;

    public UpdatedComicByUserAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public UpdatedComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_upload_comic, parent, false);
        return new UpdatedComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatedComicViewHolder holder, int position) {

        Comic comic = comicList.get(position);
        holder.lblComicTitle.setText(comic.getTitle());
        AuthorsDB author = new AuthorsDB();
        author.getAuthor(comic.getAuthor_id(), author1 -> {
            holder.lblCommentUser.setText(author1.getName());
        });
        holder.lblCommentContent.setText(comic.getDescription());

        // Convert created_at from milliseconds to formatted date
        holder.lblCommentTime.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(comic.getCreated_at())));

        Glide.with(context)
                .load(comic.getImg_url())
                .placeholder(R.drawable.truyen1)
                .into(holder.imgCommentComic);
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public static class UpdatedComicViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCommentComic;
        TextView lblComicTitle, lblCommentUser, lblCommentContent, lblCommentTime;

        public UpdatedComicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCommentComic = itemView.findViewById(R.id.imgCommentComic);
            lblComicTitle = itemView.findViewById(R.id.lblComicTitle);
            lblCommentUser = itemView.findViewById(R.id.lblCommentUser);
            lblCommentContent = itemView.findViewById(R.id.lblCommentContent);
            lblCommentTime = itemView.findViewById(R.id.lblCommentTime);
        }
    }

    public void updateComics(List<Comic> newComics) {
        this.comicList = newComics;
        notifyDataSetChanged();
    }
}
