package com.example.a4tcomic.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.ComicAdapter;
import com.example.a4tcomic.db.ComicsDB;
import com.example.a4tcomic.models.Comic;

import java.util.List;

public class ListComicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ComicAdapter comicAdapter;
    private ComicsDB comicsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comic);

        recyclerView = findViewById(R.id.recyclerListCommic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        comicAdapter = new ComicAdapter(this);
        recyclerView.setAdapter(comicAdapter);

        comicsDB = new ComicsDB();

        String authorId = getIntent().getStringExtra("authorId");
        Toast.makeText(ListComicActivity.this, authorId, Toast.LENGTH_SHORT).show();

        comicsDB.getComicsByAuthorId(authorId, new ComicsDB.AllComicsCallback() {
            @Override
            public void onAllComicsLoaded(List<Comic> comics) {
                if (comics != null && !comics.isEmpty()) {
                    // Update RecyclerView with the loaded comics
                    comicAdapter.setComics(comics);
                } else {
                    // Handle case where no comics are found for the author
                    Log.d("ListComicActivity", "No comics found for authorId: " + authorId);
                    Toast.makeText(ListComicActivity.this, "Không tìm thấy truyện của tác giả", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
