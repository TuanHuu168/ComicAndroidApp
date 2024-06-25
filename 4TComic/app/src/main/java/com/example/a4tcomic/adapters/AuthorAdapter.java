package com.example.a4tcomic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a4tcomic.R;
import com.example.a4tcomic.models.Author;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {

    private List<Author> authors;

    public AuthorAdapter(List<Author> authors) {
        this.authors = authors;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_comic, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = authors.get(position);
        holder.authorNameTextView.setText(author.getName());
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    static class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView authorNameTextView;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
//            authorNameTextView = itemView.findViewById(R.id.author_name);
        }
    }

    public void updateAuthors(List<Author> authors) {
        this.authors = authors;
        notifyDataSetChanged();
    }
}
