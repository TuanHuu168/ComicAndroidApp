package com.example.a4tcomic.activities.personal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.UpdatedComicByUserAdapter;
import com.example.a4tcomic.db.ComicsDB;
import com.example.a4tcomic.models.Comic;

import java.util.ArrayList;
import java.util.List;

public class HomeUploadActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UpdatedComicByUserAdapter adapter;
    private ComicsDB comicsDB;
    private ImageButton btnBack, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_upload);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAddComic);
        recyclerView = findViewById(R.id.uploaded_comics_by_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Comic> comicList = new ArrayList<>();
        adapter = new UpdatedComicByUserAdapter(this, comicList);
        recyclerView.setAdapter(adapter);

        comicsDB = new ComicsDB();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");

        btnBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v -> {
            Intent uploadComic = new Intent(HomeUploadActivity.this, UploadComicActivity.class);
            startActivity(uploadComic);
        });

        comicsDB.getComicByUserId(userId, new ComicsDB.AllComicsCallback() {
            @Override
            public void onAllComicsLoaded(List<Comic> comics) {
                adapter.updateComics(comics);
            }
        });
    }
}