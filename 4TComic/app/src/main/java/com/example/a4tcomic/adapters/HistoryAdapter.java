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
import com.example.a4tcomic.models.ListBookCase;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<ListBookCase> list;
    private boolean isEditMode = false;

    public HistoryAdapter(List<ListBookCase> list) {
        this.list = list;
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
        ListBookCase listBookCase = list.get(position);
        holder.bind(listBookCase);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageButton imgBtnItemHistory;
        TextView tvNameHistory;
        RadioButton radioButtonHistory;
        LinearLayout lyReadingChapter;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnItemHistory = itemView.findViewById(R.id.imgBtnItemHistory);
            tvNameHistory = itemView.findViewById(R.id.titleItemHistory);
            radioButtonHistory = itemView.findViewById(R.id.radio_button_book_History);
            lyReadingChapter = itemView.findViewById(R.id.lyReadingChapter);
        }

        public void bind(ListBookCase listBookCase) {
//            Log.d("Adapter", "isEditMode: " + isEditMode);
            imgBtnItemHistory.setImageResource(listBookCase.getImageBook());
            tvNameHistory.setText(listBookCase.getNameBook());
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
