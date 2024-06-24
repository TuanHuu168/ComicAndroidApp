package com.example.a4tcomic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a4tcomic.R;
import com.example.a4tcomic.models.Comment;
import com.example.a4tcomic.utilities.UserUtil;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private Context mContext;
    private int mResource;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        Comment comment = getItem(position);

        TextView textViewContent = listItem.findViewById(R.id.tvContentComment);
        textViewContent.setText(comment.getBody());

        TextView tvNameUser = listItem.findViewById(R.id.tvNameUser);
        tvNameUser.setTag(comment.getUser_id()); // Assuming user_id is a String

        // Initially set a placeholder text
        tvNameUser.setText("Loading...");

        // Fetch username asynchronously
        UserUtil.fetchUsername(comment.getUser_id(), new UserUtil.OnUserFetchListener() {
            @Override
            public void onUserFetched(String username) {
                // Check if the listItem is still associated with the correct user_id
                Object tag = tvNameUser.getTag();
                if (tag instanceof String && tag.equals(comment.getUser_id())) {
                    tvNameUser.setText(username);
                }
            }

            @Override
            public void onUserFetchFailed(String errorMessage) {
                // Handle fetch failure
                tvNameUser.setText("Unknown User");
            }
        });

        return listItem;
    }
}
