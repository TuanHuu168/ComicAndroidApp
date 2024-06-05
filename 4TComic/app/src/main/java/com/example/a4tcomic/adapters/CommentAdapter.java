package com.example.a4tcomic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a4tcomic.R;
import com.example.a4tcomic.models.CommentItem;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<CommentItem> {
    private Context context;
    private int resource;
    private List<CommentItem> commentList;

    public CommentAdapter(Context context, int resource, List<CommentItem> commentList) {
        super(context, resource, commentList);
        this.context = context;
        this.resource = resource;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }

        CommentItem commentItem = commentList.get(position);

        ImageView commentImage = view.findViewById(R.id.imgCommentComic);
        TextView commentTitle = view.findViewById(R.id.lblComicTitle);
        TextView commentUser = view.findViewById(R.id.lblCommentUser);
        TextView commentContent = view.findViewById(R.id.lblCommentContent);
        TextView commentTime = view.findViewById(R.id.lblCommentTime);

        commentImage.setImageResource(commentItem.getImageResource());
        commentTitle.setText(shortenTitle(commentItem.getTitle()));
        commentUser.setText(commentItem.getUser());
        commentContent.setText(shortenContent(commentItem.getContent()));
        commentTime.setText(commentItem.getTime());

        return view;
    }

    private String shortenContent(String content) {
        if (content.length() > 70) {
            return content.substring(0, 70) + "...";
        } else {
            return content;
        }
    }

    private String shortenTitle(String title) {
        if (title.length() > 27) {
            return title.substring(0, 27) + "...";
        } else {
            return title;
        }
    }
}
