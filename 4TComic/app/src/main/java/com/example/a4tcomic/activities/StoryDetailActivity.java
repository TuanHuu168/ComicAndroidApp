package com.example.a4tcomic.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.a4tcomic.models.Chapter;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailActivity extends AppCompatActivity {

    private Button btn_favorite;
    private ImageView img_banner, img_title;
    private TextView des_tv;
    private RecyclerView rcv_genres, rcv_chapters;
    private GenreAdapter genreAdapter;
    private ChapterAdapter chapterAdapter;

    private boolean isExpanded = false;
    private boolean isFavorite = false;

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
        img_banner = findViewById(R.id.img_banner);
        img_title = findViewById(R.id.img_title);
        btn_favorite = findViewById(R.id.btn_favorite);
        rcv_genres = findViewById(R.id.rcv_genres);
        des_tv = findViewById(R.id.des_tv);
        rcv_chapters = findViewById(R.id.rcv_chapters);

        // Back
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        // Banner
        img_banner.setImageResource(R.drawable.banner);

        // Title
        img_title.setImageResource(R.drawable.img_title);

        // Yêu thích
        btn_favorite.setOnClickListener(v -> {
            if (!isFavorite) {
                btn_favorite.setText(R.string.btn_favorited);
                btn_favorite.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                btn_favorite.setTextColor(ContextCompat.getColor(this,R.color.pink_main));
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited, 0, 0, 0);
                isFavorite = true;
            } else {
                btn_favorite.setText(R.string.btn_favorite);
                btn_favorite.setBackgroundColor(ContextCompat.getColor(this,R.color.pink_main));
                btn_favorite.setTextColor(ContextCompat.getColor(this,R.color.white));
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_white, 0, 0, 0);
                isFavorite = false;
            }
        });

        // Thể loại
        genreAdapter = new GenreAdapter();
        GridLayoutManager gLM = new GridLayoutManager(this, 3);
        rcv_genres.setLayoutManager(gLM);
        rcv_genres.setFocusable(false);
        rcv_genres.setNestedScrollingEnabled(false);
        genreAdapter.setData(getGenres());
        rcv_genres.setAdapter(genreAdapter);

        // Nội dung
        String description = "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm " +
                "- Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu" +
                "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm " +
                "- Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu" +
                "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm " +
                "- Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu" +
                "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm " +
                "- Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu" ;
        des_tv.setText(description);
        des_tv.setOnClickListener(v -> {
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
        });

        // Chương
        chapterAdapter = new ChapterAdapter(this);
        LinearLayoutManager lLM = new LinearLayoutManager(this);
        rcv_chapters.setLayoutManager(lLM);
        rcv_chapters.setFocusable(false); // Không cho focus vào RecyclerView
        rcv_chapters.setNestedScrollingEnabled(false);
        chapterAdapter.setData(getChapters());
        rcv_chapters.setAdapter(chapterAdapter);
    }

    private List<String> getGenres() {
        List<String> genres = new ArrayList<>();

        genres.add("Hành động");
        genres.add("Phiêu lưu");
        genres.add("Shounen");
        genres.add("Hài hước");
        genres.add("Drama");

        return genres;
    }

    private List<Chapter> getChapters() {
        List<Chapter> chapters = new ArrayList<>();

        chapters.add(new Chapter(R.drawable.chapter_1, "Chapter 1", "21/2/2003"));
        chapters.add(new Chapter(R.drawable.chapter_2, "Chapter 2", "22/2/2003"));
        chapters.add(new Chapter(R.drawable.chapter_3, "Chapter 3", "23/2/2003"));
        chapters.add(new Chapter(R.drawable.chapter_4, "Chapter 4", "24/2/2003"));

        chapters.add(new Chapter(R.drawable.chapter_1, "Chapter 1", "21/2/2003"));
        chapters.add(new Chapter(R.drawable.chapter_2, "Chapter 2", "22/2/2003"));
        chapters.add(new Chapter(R.drawable.chapter_3, "Chapter 3", "23/2/2003"));
        chapters.add(new Chapter(R.drawable.chapter_4, "Chapter 4", "24/2/2003"));

        return chapters;
    }
}