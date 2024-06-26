package com.example.a4tcomic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.a4tcomic.R;
import com.example.a4tcomic.activities.search.FindByWriterActivity;
import com.example.a4tcomic.activities.search.ListComicActivity;
import com.example.a4tcomic.adapters.ContentAdapter;
import com.example.a4tcomic.db.ComicsDB;
import com.example.a4tcomic.db.FavoritesDB;
import com.example.a4tcomic.db.HistoryDB;
import com.example.a4tcomic.models.Chapter;
import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.History;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity{

    // Danh sách truyện
    private RecyclerView trendingRecyclerView;
    private RecyclerView historyRecyclerView;
    private RecyclerView recentlyUpdatedRecyclerView;
    private List<Comic> trendingList;
    private List<Comic> historyList;
    private List<Comic> recentlyUpdatedList;

    // Các biến khác
    EditText et_search;
    ImageButton btnAdvancedSearch, btnHomePage, btnArchive, btnNotification, btnSetting, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        trendingRecyclerView = findViewById(R.id.recyclerViewTrending);
        historyRecyclerView = findViewById(R.id.recyclerViewHistory);
        recentlyUpdatedRecyclerView = findViewById(R.id.recyclerViewRecentlyUpdated);
        btnAdvancedSearch = findViewById(R.id.btnAdvancedSearch);
        btnSearch = findViewById(R.id.btnSearch);

        btnHomePage = findViewById(R.id.btnHomePage);
        btnArchive = findViewById(R.id.btnArchive);
        btnNotification = findViewById(R.id.btnNotification);
        btnSetting = findViewById(R.id.btnSetting);

        btnNotification.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, NotificationActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnSetting.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, PersonalActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnArchive.setOnClickListener(v -> {
            Intent archiveIntent = new Intent(this, BookcaseActivity.class);
            startActivity(archiveIntent);
            finish();
        });

        btnAdvancedSearch.setOnClickListener(v -> {
            Intent advancedSearchIntent = new Intent(this, FindByWriterActivity.class);
            startActivity(advancedSearchIntent);
        });

        btnSearch.setOnClickListener(v -> {
            Intent searchIntent = new Intent(this, ListComicActivity.class);
            startActivity(searchIntent);
        });

        // Tạo slider vào thêm ảnh vào slider
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slider_1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider_2, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider_3, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider_4, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);

        // Tạo custom listView
        trendingList = new ArrayList<>();
        historyList = new ArrayList<>();
        recentlyUpdatedList = new ArrayList<>();

        // Lấy nội dung truyện hot từ Firebase
        getTrendingComics();

        // Thêm nội dung lịch sử đọc
        getUserHistoryComics();

        // Thêm nội dung truyện mới tải lên
        getRecentlyUpdatedComics();

        // Thiết lập Adapter và LayoutManager cho RecyclerView
        setupRecyclerView(trendingRecyclerView, trendingList);
        setupRecyclerView(historyRecyclerView, historyList);
        setupRecyclerView(recentlyUpdatedRecyclerView, recentlyUpdatedList);
    }

    private void getTrendingComics() {
        FavoritesDB favoritesDB = new FavoritesDB();
        favoritesDB.getComicsMostFavor(comics -> {
            trendingList.clear();
            trendingList.addAll(comics);
            trendingRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void getUserHistoryComics(){
        HistoryDB historyDB = new HistoryDB();
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");
        historyDB.getComicsReadByUser(userId, (history, comics, chapter) -> {
            historyList.clear();
            historyList.addAll(comics);
            historyRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void getRecentlyUpdatedComics(){
        ComicsDB comicsDB = new ComicsDB();
        comicsDB.getAllComics(comics ->{
            recentlyUpdatedList.clear();
            recentlyUpdatedList.addAll(comics);
            recentlyUpdatedRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Comic> itemList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ContentAdapter adapter = new ContentAdapter(this, itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
