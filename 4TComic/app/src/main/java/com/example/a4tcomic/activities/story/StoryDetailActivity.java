package com.example.a4tcomic.activities.story;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.ChapterAdapter;
import com.example.a4tcomic.adapters.GenreAdapter;
import com.example.a4tcomic.db.AuthorsDB;
import com.example.a4tcomic.db.ChaptersDB;
import com.example.a4tcomic.db.FavoritesDB;
import com.example.a4tcomic.db.GenresDB;
import com.example.a4tcomic.models.Chapter;
import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.Genre;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailActivity extends AppCompatActivity {

    private Button btn_first_chapter, btn_cmt, btn_last_chapter;
    private ImageView img_banner, img_title;
    private TextView tv_name, tv_author, des_tv, btn_favorite;

    private RecyclerView rcv_genres, rcv_chapters;
    private GenreAdapter genreAdapter;
    private ChapterAdapter chapterAdapter;

    private AuthorsDB authorsDB;
    private FavoritesDB favoritesDB;
    private GenresDB genresDB;
    private ChaptersDB chaptersDB;

    private Comic currentComic;
    private String user_id = "";
    private String comic_id = "";
    private String id_favorite = "";
    private boolean isExpanded = false;
    private boolean isFavorite = false;
    private List<Genre> genres;
    private List<Chapter> chapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tham chiếu đến các View
        tv_name = findViewById(R.id.tv_name_story);
        tv_author = findViewById(R.id.tv_author_story);
        img_banner = findViewById(R.id.img_banner);
        img_title = findViewById(R.id.img_title);
        btn_favorite = findViewById(R.id.btn_favorite);
        rcv_genres = findViewById(R.id.rcv_genres);
        des_tv = findViewById(R.id.des_tv);
        rcv_chapters = findViewById(R.id.rcv_chapters);
        btn_first_chapter = findViewById(R.id.btn_first_chapter);
        btn_cmt = findViewById(R.id.btn_cmt);
        btn_last_chapter = findViewById(R.id.btn_last_chapter);

        // Back
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        // Thể loại
        genres = new ArrayList<>();
        genreAdapter = new GenreAdapter();
        GridLayoutManager gLM = new GridLayoutManager(this, 3);
        rcv_genres.setLayoutManager(gLM);

        // Chương
        chapters = new ArrayList<>();
        chapterAdapter = new ChapterAdapter(chapter -> {
            goToReadPage(chapter);
        });
        LinearLayoutManager lLM = new LinearLayoutManager(this);
        rcv_chapters.setLayoutManager(lLM);

        authorsDB = new AuthorsDB();
        favoritesDB = new FavoritesDB();
        genresDB = new GenresDB();
        chaptersDB = new ChaptersDB();

        // get data
        currentComic = (Comic) getIntent().getSerializableExtra("comic");
        user_id = currentComic.getUser_id();
        comic_id = currentComic.getId();
        setupUI();

        // Yêu thích
        btn_favorite.setOnClickListener(v -> {
            setFavorite();
            setFavoriteBtn();
        });

        // Mô tả
        des_tv.setOnClickListener(v -> {
            setExpanded();
        });

        // Button bottom
        btn_last_chapter.setOnClickListener(v -> {
            Chapter lastChapter = chapters.isEmpty() ? null : chapters.get(0);
            goToReadPage(lastChapter);
        });

        btn_first_chapter.setOnClickListener(v -> {
            Chapter firstChapter = chapters.isEmpty() ? null : chapters.get(chapters.size() - 1);
            goToReadPage(firstChapter);

        });

        btn_cmt.setOnClickListener(v -> {
            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra("comic_id", comic_id);
            startActivity(intent);
        });

    }

    private void goToReadPage(Chapter chapter) {
        if (chapter == null) {
            Toast.makeText(this, "Truyện không có chương nào", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ReadPageActivity.class);
        intent.putExtra("chapterList", (ArrayList<Chapter>) chapters);
        intent.putExtra("chapter", chapter);
        startActivity(intent);
    }

    private void setupUI() {
        // Banner
        if (!currentComic.getBanner_url().equals("")) {
            Picasso.get().load(currentComic.getBanner_url()).into(img_banner);
        }
        // Img title
        if (!currentComic.getImg_url().equals("")) {
            Picasso.get().load(currentComic.getImg_url()).into(img_title);
        }
        // Name
        tv_name.setText(currentComic.getTitle());
        // Author
        authorsDB.getAuthor(
                currentComic.getAuthor_id(),
                new_author -> tv_author.setText(new_author.getName()));
        // Favorite
        getFavorite();
        // Thể loại
        getGenres();
        // Description
        des_tv.setText(currentComic.getDescription());
        // Chương
        getChapters();
    }

    private void getFavorite() {
        favoritesDB.getFavorite(user_id, comic_id, id -> {
            if (!id.equals("")) {
                id_favorite = id;
                isFavorite = true;
                setFavoriteBtn();
            }
            else {
                isFavorite = false;
                setFavoriteBtn();
            }
        });
    }

    private void setFavoriteBtn() {
        if (isFavorite) {
            btn_favorite.setText(R.string.btn_favorited);
            btn_favorite.setSelected(true);
            btn_favorite.setTextColor(ContextCompat.getColor(this,R.color.pink_main));
            btn_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited, 0, 0, 0);

        } else {
            btn_favorite.setText(R.string.btn_favorite);
            btn_favorite.setTextColor(ContextCompat.getColor(this,R.color.white));
            btn_favorite.setSelected(false);
            btn_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_white, 0, 0, 0);
        }
    }

    private void setFavorite() {
        long created_at = System.currentTimeMillis();
        if (isFavorite){
            favoritesDB.removeFavorite(id_favorite);
            isFavorite = false;
        }
        else {
            favoritesDB.addFavorite(comic_id, user_id, created_at);
            isFavorite = true;
        }
    }

    private void setExpanded() {
        if (!isExpanded) {
            des_tv.setMaxLines(Integer.MAX_VALUE);
            des_tv.setEllipsize(null);
            des_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.up);
            isExpanded = true;
        } else {
            des_tv.setMaxLines(3);
            des_tv.setEllipsize(TextUtils.TruncateAt.END);
            des_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.down);
            isExpanded = false;
        }
    }

    private void getGenres() {
        rcv_genres.setFocusable(false);
        rcv_genres.setNestedScrollingEnabled(false);
        genreAdapter.setData(genres);
        rcv_genres.setAdapter(genreAdapter);

        genresDB.getGenresByComicId(comic_id, genreList -> {
            genres.clear();
            genres.addAll(genreList);
            genreAdapter.notifyDataSetChanged();
        });
    }

    private void getChapters() {
        rcv_chapters.setFocusable(false);
        rcv_chapters.setNestedScrollingEnabled(false);
        chapterAdapter.setData(chapters);
        rcv_chapters.setAdapter(chapterAdapter);

        chaptersDB.getChapters(comic_id, getChapter -> {
            chapters.clear();
            chapters.addAll(getChapter);
            chapterAdapter.notifyDataSetChanged();
        });
    }
}