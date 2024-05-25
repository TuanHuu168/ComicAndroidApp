package com.example.a4tcomic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.AppViewHolder>{
    private List<ListBookCase> list;
    private boolean isEditMode = false;

    public void setEditMode(boolean isEditMode){
        this.isEditMode = isEditMode;
        notifyDataSetChanged(); // cập nhật lại recyclerView
    }
    public void selectAll(){
        for(ListBookCase book : list){
            book.setChecked(true);
        }
        notifyDataSetChanged(); // cập nhật lại revycler view để hiển thị trang thái đã chọn
    }
    public void deselectAll(){
        for(ListBookCase book : list){
            book.setChecked(false);
        }
        notifyDataSetChanged(); // cập nhật lại revycler view để hiển thị trang thái bỏ chọn
    }
    public BookAdapter(List<ListBookCase> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_bookcase,parent,false);
        return new AppViewHolder(view);
    }

    // Xử lý su kiện radio button chọn và không chọn
    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        ListBookCase listBookCase = list.get(position);
        if(listBookCase!= null){
            holder.imgBtnItem.setImageResource(listBookCase.getImageBook());
            holder.tvName.setText(listBookCase.getNameBook());
            holder.radioButton.setChecked(listBookCase.isChecked());
            holder.radioButton.setVisibility(isEditMode?View.VISIBLE:View.GONE);
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listBookCase.setChecked(!listBookCase.isChecked());
                    holder.radioButton.setChecked(listBookCase.isChecked());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder{
        ImageButton imgBtnItem;
        TextView tvName;
        RadioButton radioButton;
        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnItem  = itemView.findViewById(R.id.imgBtnItem);
            tvName = itemView.findViewById(R.id.titleItem);
            radioButton = itemView.findViewById(R.id.radio_button_book);
        }
    }
}
