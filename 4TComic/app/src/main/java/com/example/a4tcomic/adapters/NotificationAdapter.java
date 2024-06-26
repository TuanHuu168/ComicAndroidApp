package com.example.a4tcomic.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.a4tcomic.R;
import com.example.a4tcomic.db.ChaptersDB;
import com.example.a4tcomic.db.FavoritesDB;
import com.example.a4tcomic.models.Chapter;
import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.Favorite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final Context context;
    private final List<Chapter> chapters;
    private final Map<String, Comic> comicMap;
    private final FavoritesDB favoritesDB;
    private final ChaptersDB chaptersDB;
    private final String userId;

    public NotificationAdapter(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        this.chapters = new ArrayList<>();
        this.comicMap = new HashMap<>();
        this.favoritesDB = new FavoritesDB();
        this.chaptersDB = new ChaptersDB();
        loadFavoriteComics();
    }

    public void setComics(List<Comic> comics) {
        comicMap.clear();
        if (comics != null) {
            for (Comic comic : comics) {
                comicMap.put(comic.getId(), comic);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        if (chapters == null || chapters.isEmpty()) {
            Log.d("NotificationAdapter", "Chapters list is empty or null");
            return;
        }

        Chapter chapter = chapters.get(position);
        Comic comic = comicMap.get(chapter.getComic_id());

        if (comic != null) {
            holder.tvNameComic.setText(comic.getTitle());
            if (comic.getImg_url() != null && !comic.getImg_url().isEmpty()) {
                Glide.with(context)
                        .load(comic.getImg_url())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.truyen3)
                                .error(R.drawable.truyen3))
                        .into(holder.imvComic);
            } else {
                Glide.with(context)
                        .load(R.drawable.truyen3)
                        .into(holder.imvComic);
            }
        } else {
            holder.tvNameComic.setText("Unknown Comic");
            Glide.with(context)
                    .load(R.drawable.truyen3)
                    .into(holder.imvComic);
        }

        holder.tvNameChapter.setText(chapter.getTitle());
        holder.tvTime.setText(getTimeElapsed(chapter.getCreated_at()));
    }

    @Override
    public int getItemCount() {
        return chapters != null ? chapters.size() : 0;
    }

    public void setNotifications(List<Chapter> chapters) {
        this.chapters.clear();
        this.chapters.addAll(chapters);
        notifyDataSetChanged();
    }

    private String getTimeElapsed(long createdAt) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - createdAt;

        long days = TimeUnit.MILLISECONDS.toDays(elapsedTime);
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;

        if (days > 0) {
            return days + " days ago";
        } else if (hours > 0) {
            return hours + " hours ago";
        } else if (minutes > 0) {
            return minutes + " minutes ago";
        } else {
            return seconds + " seconds ago";
        }
    }

    private void loadFavoriteComics() {
        favoritesDB.getComicsFavorByUser(userId, new FavoritesDB.FavoriteComicsCallback() {
            @Override
            public void onFavoriteComicsLoaded(List<Favorite> favorites, List<Comic> comics) {
                setComics(comics);
                List<Chapter> newChapters = new ArrayList<>();
                for (Favorite favorite : favorites) {
                    chaptersDB.getChapters(favorite.getComic_id(), new ChaptersDB.ChaptersCallback() {
                        @Override
                        public void onChaptersLoaded(List<Chapter> chapterList) {
                            for (Chapter chapter : chapterList) {
                                if (chapter.getCreated_at() > favorite.getCreated_at()) {
                                    newChapters.add(chapter);
                                }
                            }
                            setNotifications(newChapters);
                        }
                    });
                }
            }
        });
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView imvComic;
        TextView tvNameComic, tvNameChapter, tvTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imvComic = itemView.findViewById(R.id.imvComic);
            tvNameComic = itemView.findViewById(R.id.tvNameComic);
            tvNameChapter = itemView.findViewById(R.id.tvNameChapter);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
