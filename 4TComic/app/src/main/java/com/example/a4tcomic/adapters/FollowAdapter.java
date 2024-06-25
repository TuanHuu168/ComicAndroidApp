package com.example.a4tcomic.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.models.ListBookCase;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {
    private List<ListBookCase> list;
    private boolean isEditMode = false;

    public FollowAdapter(List<ListBookCase> list) {
        this.list = list;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        Log.d("Adapter", "isEditMode: " + isEditMode);
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
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_story_follow, parent, false);
        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        ListBookCase listBookCase = list.get(position);
        holder.bind(listBookCase);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FollowViewHolder extends RecyclerView.ViewHolder {
        ImageButton imgBtnItemFollow;
        TextView tvNameFollow;
        RadioButton radioButtonFollow;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnItemFollow = itemView.findViewById(R.id.imgBtnItemFollow);
            tvNameFollow = itemView.findViewById(R.id.titleItemFollow);
            radioButtonFollow = itemView.findViewById(R.id.radio_button_book_Follow);
        }

        public void bind(ListBookCase listBookCase) {
            Log.d("Adapter", "isEditMode: " + isEditMode);
           // imgBtnItemFollow.setImageResource(listBookCase.getImageBook());
            tvNameFollow.setText(listBookCase.getNameBook());
            radioButtonFollow.setChecked(listBookCase.isChecked());
            radioButtonFollow.setVisibility(isEditMode ? View.VISIBLE : View.INVISIBLE);
            radioButtonFollow.setOnClickListener(v -> {
                listBookCase.setChecked(!listBookCase.isChecked());
                radioButtonFollow.setChecked(listBookCase.isChecked());
            });
        }
    }
}
