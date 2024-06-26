package com.example.a4tcomic.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a4tcomic.R;
import com.example.a4tcomic.db.CommentsDB;
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.Comment;
import com.example.a4tcomic.models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context mContext;
    private final List<Comment> commentList;
    private final CommentsDB commentsDB;
    private final UsersDB usersDB;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
        this.commentList = new ArrayList<>();
        this.commentsDB = new CommentsDB();
        this.usersDB = new UsersDB();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_2, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        String timeAgo = getTimeAgo(comment.getCreated_at());
        holder.tvContentComment.setText(comment.getBody());
        holder.tvTime.setText(timeAgo);

        usersDB.getUserById(comment.getUser_id(), new UsersDB.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                holder.tvNameUser.setText(user.getUsername());
                Glide.with(mContext)
                        .load(user.getAvatar_url())
                        .placeholder(R.drawable.truyen1)
                        .into(holder.imgUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private String getTimeAgo(long time) {
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public void loadComments(String comicId) {
        commentsDB.getComments(comicId, new CommentsDB.CommentsCallback() {
            @Override
            public void onCommentsLoaded(List<Comment> comments) {
                commentList.clear();
                commentList.addAll(comments);
                notifyDataSetChanged();
            }
        });
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvContentComment, tvNameUser, tvTime;
        ImageView imgUser;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentComment = itemView.findViewById(R.id.tvContentComment);
            tvNameUser = itemView.findViewById(R.id.tvNameUser);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}