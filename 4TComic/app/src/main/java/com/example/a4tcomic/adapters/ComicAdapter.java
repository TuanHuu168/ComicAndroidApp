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
import com.example.a4tcomic.models.Comic;

import java.util.ArrayList;

public class ComicAdapter extends ArrayAdapter<Comic> {
    private Context mContext;
    private ArrayList<Comic> mList;

    public ComicAdapter(Context context, ArrayList<Comic> list) {
        super(context, 0, list);
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_comic, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = convertView.findViewById(R.id.tvNameComic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Comic currentComic = mList.get(position);
        viewHolder.nameTextView.setText(currentComic.getTitle());

        return convertView;
    }

    static class ViewHolder {
        TextView nameTextView;
    }
}

