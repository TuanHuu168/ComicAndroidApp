package com.example.a4tcomic;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;


public class HomePageActivity extends AppCompatActivity {

    private RecyclerView trendingRecyclerView;
    private RecyclerView historyRecyclerView;
    private RecyclerView recentlyUpdatedRecyclerView;
    private List<ComicItem> trendingList;
    private List<ComicItem> historyList;
    private List<ComicItem> recentlyUpdatedList;


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
        trendingRecyclerView = findViewById(R.id.recycler_view_trending);
        historyRecyclerView = findViewById(R.id.recycler_view_history);
        recentlyUpdatedRecyclerView = findViewById(R.id.recycler_view_recently_updated);

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
        // Thêm nội dung truyện
        trendingList.add(new ComicItem(R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!"));
        trendingList.add(new ComicItem(R.drawable.truyen2, "Tinh Tú Kiếm Sĩ"));
        trendingList.add(new ComicItem(R.drawable.truyen4, "Không Chỉ Là Bắt Nạt"));

        historyList.add(new ComicItem(R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!"));
        historyList.add(new ComicItem(R.drawable.truyen2, "Tinh Tú Kiếm Sĩ"));
        historyList.add(new ComicItem(R.drawable.truyen3, "Tôi Trở Nên Phi Thường Ngay Cả Ở Thế Giới Thật"));
        historyList.add(new ComicItem(R.drawable.truyen4, "Không Chỉ Là Bắt Nạt"));

        recentlyUpdatedList.add(new ComicItem(R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!"));
        recentlyUpdatedList.add(new ComicItem(R.drawable.truyen2, "Tinh Tú Kiếm Sĩ"));
        recentlyUpdatedList.add(new ComicItem(R.drawable.truyen3, "Tôi Trở Nên Phi Thường Ngay Cả Ở Thế Giới Thật"));
        recentlyUpdatedList.add(new ComicItem(R.drawable.truyen4, "Không Chỉ Là Bắt Nạt"));

        // Thiết lập Adapter và LayoutManager cho RecyclerView
        setupRecyclerView(trendingRecyclerView, trendingList);
        setupRecyclerView(historyRecyclerView, historyList);
        setupRecyclerView(recentlyUpdatedRecyclerView, recentlyUpdatedList);

    }

    private void setupRecyclerView(RecyclerView recyclerView, List<ComicItem> itemList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ComicAdapter adapter = new ComicAdapter(this, itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}