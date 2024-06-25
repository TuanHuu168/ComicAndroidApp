package com.example.a4tcomic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.models.Chapter;
import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.History;
import com.example.a4tcomic.models.ListBookCase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<ListBookCase> list;
    private List<History> listHistory;
    private List<Chapter> listChapter;
    private List<Comic> listComic;
    private boolean isEditMode = false;

    public HistoryAdapter() {
    }

    public void setData(List<History> listHistory, List<Comic> listComic , List<Chapter> listChapter) {
        this.listHistory = listHistory;
        this.listChapter = listChapter;
        this.listComic = listComic;
        this.list = new ArrayList<>();
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (ListBookCase book : list) {
            book.setChecked(true);
        }
        notifyDataSetChanged();
    }

    public void deselectAll() {
        for (ListBookCase book : list) {
            book.setChecked(false);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_story_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Chapter chapter = listChapter.get(position);
        Comic comic = listComic.get(position);
        History history = listHistory.get(position); // lấy id History

        ListBookCase listBookCase = new ListBookCase(comic.getImg_url(), comic.getTitle(), chapter.getOrder());
        this.list.add(listBookCase);

        holder.onBindView(history, comic, chapter, listBookCase);
    }


    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageButton imgBtnItemHistory;
        TextView tvNameHistory;
        RadioButton radioButtonHistory;
        LinearLayout lyReadingChapter;
        TextView valueChapterHistory;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnItemHistory = itemView.findViewById(R.id.imgBtnItemHistory);
            tvNameHistory = itemView.findViewById(R.id.titleItemHistory);
            radioButtonHistory = itemView.findViewById(R.id.radio_button_book_History);
            lyReadingChapter = itemView.findViewById(R.id.lyReadingChapter);
            valueChapterHistory = itemView.findViewById(R.id.valueChapterHistory);
        }

        public void onBindView(History history, Comic comic, Chapter chapter, ListBookCase listBookCase) {
            if (!comic.getImg_url().isEmpty()) {
                Picasso.get().load(comic.getImg_url()).into(imgBtnItemHistory);
            }
            tvNameHistory.setText(comic.getTitle());
            valueChapterHistory.setText(chapter.getOrder()+"");

            radioButtonHistory.setChecked(listBookCase.isChecked());
            radioButtonHistory.setVisibility(isEditMode ? View.VISIBLE : View.INVISIBLE);
            radioButtonHistory.setOnClickListener(v -> {
                listBookCase.setChecked(!listBookCase.isChecked());
                radioButtonHistory.setChecked(listBookCase.isChecked());
            });
            lyReadingChapter.setVisibility(View.VISIBLE);
        }

    }
}
