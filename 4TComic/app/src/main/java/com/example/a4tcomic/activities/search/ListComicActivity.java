package com.example.a4tcomic.activities.search;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.ComicAdapter;
import com.example.a4tcomic.db.ComicsDB;
import com.example.a4tcomic.db.GenresDB;
import com.example.a4tcomic.models.Comic;

import java.util.List;

public class ListComicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ComicAdapter comicAdapter;
    private ImageButton btnSearch;
    private EditText edtSearch;
    private ComicsDB comicsDB;
    private GenresDB genresDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comic);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.editSearch);
        btn_back.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerListCommic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        comicAdapter = new ComicAdapter(this);
        recyclerView.setAdapter(comicAdapter);

        comicsDB = new ComicsDB();
        genresDB = new GenresDB();

        String authorId = getIntent().getStringExtra("authorId");
        String userId = getIntent().getStringExtra("userId");
        String genreId = getIntent().getStringExtra("genreId");

        if(authorId != null){
            comicsDB.getComicsByAuthorId(authorId, new ComicsDB.AllComicsCallback() {
                @Override
                public void onAllComicsLoaded(List<Comic> comics) {
                    if (comics != null && !comics.isEmpty()) {
                        comicAdapter.setComics(comics);
                    } else {
                        Log.d("ListComicActivity", "No comics found for authorId: " + authorId);
                        Toast.makeText(ListComicActivity.this, "Không tìm thấy truyện của tác giả", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (userId != null) {
            comicsDB.getComicByUserId(userId, new ComicsDB.AllComicsCallback() {
                @Override
                public void onAllComicsLoaded(List<Comic> comics) {
                    if (comics != null && !comics.isEmpty()) {
                        comicAdapter.setComics(comics);
                    } else {
                        Log.d("ListComicActivity", "No comics found for userId: " + userId);
                        Toast.makeText(ListComicActivity.this, "Không tìm thấy truyện của người dịch", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (genreId != null) {
         genresDB.getComicsByGenreId(genreId, new ComicsDB.AllComicsCallback() {
             @Override
             public void onAllComicsLoaded(List<Comic> comics) {
                 if (comics != null && !comics.isEmpty()) {
                     comicAdapter.setComics(comics);
                 }
                 else {
                     Log.d("ListComicActivity", "No comics found for genreId: " + genreId);
                     Toast.makeText(ListComicActivity.this, "Không tìm thấy truyện của thể loại", Toast.LENGTH_SHORT).show();
                 }
             }
         });
        }
        else{
            comicsDB.getAllComics(new ComicsDB.AllComicsCallback() {
                @Override
                public void onAllComicsLoaded(List<Comic> comics) {
                    if (comics != null && !comics.isEmpty()) {
                        comicAdapter.setComics(comics);
                    } else {
                        Log.d("ListComicActivity", "No comics found");
                        Toast.makeText(ListComicActivity.this, "Không tìm thấy truyện", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = edtSearch.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    comicsDB.getComicsByTitle(searchText, new ComicsDB.AllComicsCallback() {
                        @Override
                        public void onAllComicsLoaded(List<Comic> comics) {
                            if (comics != null && !comics.isEmpty()) {
                                comicAdapter.setComics(comics);
                            } else {
                                Log.d("ListComicActivity", "No comics found with title: " + searchText);
                                Toast.makeText(ListComicActivity.this, "Không tìm thấy truyện với tên: " + searchText, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ListComicActivity.this, "Vui lòng nhập tên truyện", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
