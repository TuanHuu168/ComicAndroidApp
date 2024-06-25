package com.example.a4tcomic.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.a4tcomic.adapters.ContentAdapter;
import com.example.a4tcomic.models.ContentItem;

import java.util.ArrayList;
import java.util.List;


public class HomePageActivity extends AppCompatActivity implements View.OnTouchListener {

    // Danh sách truyện
    private RecyclerView trendingRecyclerView;
    private RecyclerView historyRecyclerView;
    private RecyclerView recentlyUpdatedRecyclerView;
    private List<ContentItem> trendingList;
    private List<ContentItem> historyList;
    private List<ContentItem> recentlyUpdatedList;

    // Các biến khác
    EditText et_search;
    ImageButton btnAdvancedSearch, btnHomePage, btnArchive, btnNotification, btnSetting;

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
        et_search = findViewById(R.id.et_search);
        trendingRecyclerView = findViewById(R.id.recyclerViewTrending);
        historyRecyclerView = findViewById(R.id.recyclerViewHistory);
        recentlyUpdatedRecyclerView = findViewById(R.id.recyclerViewRecentlyUpdated);
        btnAdvancedSearch = findViewById(R.id.btnAdvancedSearch);

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

        // hide keyboard
        findViewById(R.id.main).setOnTouchListener(this);

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

        // Thêm nội dung truyện hot
        trendingList.add(new ContentItem(0,R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!"));
        trendingList.add(new ContentItem(0,R.drawable.truyen2, "Tinh Tú Kiếm Sĩ"));
        trendingList.add(new ContentItem(0,R.drawable.truyen4, "Không Chỉ Là Bắt Nạt"));

        // Thêm nội dung lịch sử đọc
        historyList.add(new ContentItem(0,R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!"));
        historyList.add(new ContentItem(0,R.drawable.truyen2, "Tinh Tú Kiếm Sĩ"));
        historyList.add(new ContentItem(0,R.drawable.truyen3, "Tôi Trở Nên Phi Thường Ngay Cả Ở Thế Giới Thật"));
        historyList.add(new ContentItem(0,R.drawable.truyen4, "Không Chỉ Là Bắt Nạt"));

        // Thêm nội dung truyện mới tải lên
        recentlyUpdatedList.add(new ContentItem(0,R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!"));
        recentlyUpdatedList.add(new ContentItem(0,R.drawable.truyen2, "Tinh Tú Kiếm Sĩ"));
        recentlyUpdatedList.add(new ContentItem(0,R.drawable.truyen3, "Tôi Trở Nên Phi Thường Ngay Cả Ở Thế Giới Thật"));
        recentlyUpdatedList.add(new ContentItem(0,R.drawable.truyen4, "Không Chỉ Là Bắt Nạt"));

        // Thiết lập Adapter và LayoutManager cho RecyclerView
        setupRecyclerView(trendingRecyclerView, trendingList);
        setupRecyclerView(historyRecyclerView, historyList);
        setupRecyclerView(recentlyUpdatedRecyclerView, recentlyUpdatedList);

    }

    private void setupRecyclerView(RecyclerView recyclerView, List<ContentItem> itemList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ContentAdapter adapter = new ContentAdapter(this, itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        et_search.clearFocus();
        return false;
    }
}