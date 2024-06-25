package com.example.a4tcomic.activities.story;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.ChaptersAdapter;
import com.example.a4tcomic.adapters.ReadingPagerAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReadPageActivity extends AppCompatActivity {

    ImageButton btnBack, btnMusic;
    private ViewPager2 viewPager;
    private ReadingPagerAdapter adapter;
    private List<Integer> pageImages;
    private ImageButton btnSelectChapter, btnPrevious, btnNext;
    private List<String> chapters;
    Boolean isMusicPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_page);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnMusic = findViewById(R.id.btnMusic);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMusic();
            }
        });

        // Khởi tạo danh sách các trang truyện
        pageImages = new ArrayList<>();
        pageImages.add(R.drawable.ic_page1);
        pageImages.add(R.drawable.ic_page1);
        pageImages.add(R.drawable.ic_page1);
        // Thêm các trang khác vào danh sách
        viewPager = findViewById(R.id.vpChapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL); // Đặt hướng cuộn dọc
        adapter = new ReadingPagerAdapter(pageImages);
        viewPager.setAdapter(adapter);

        btnSelectChapter = findViewById(R.id.btnChooseChapters);
        btnPrevious = findViewById(R.id.btnLeftArrow);
        btnNext = findViewById(R.id.btnRightArrow);

        // Khởi tạo danh sách các chương
        chapters = new ArrayList<>();
        for (int i = 1; i <= 165; i++) {
            chapters.add("Chương " + i);
        }

        btnSelectChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void toggleMusic() {
        Intent intent = new Intent(ReadPageActivity.this, Music.class);
        if (isMusicPlaying) {
            stopService(intent);
            btnMusic.setImageResource(R.drawable.ic_adjust); // Cập nhật icon cho nút
        } else {
            startService(intent);
            btnMusic.setImageResource(R.drawable.ic_adjust); // Cập nhật icon cho nút
        }
        isMusicPlaying = !isMusicPlaying;
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_chapters);
        RecyclerView recyclerView = dialog.findViewById(R.id.rcv_chapters);
        Button btnFilterNewest = dialog.findViewById(R.id.btn_filter_newest);
        Button btnFilterOldest = dialog.findViewById(R.id.btn_filter_oldest);

        // Thiết lập adapter và layout manager cho RecyclerView
        ChaptersAdapter adapter = new ChaptersAdapter(chapters);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Hiển thị 3 cột

        View.OnClickListener filterClickListener = new View.OnClickListener() {     // cung cấp để lắng nghe sự kiện nhấn trên 1 view
            @Override
            public void onClick(View v) {
                List<String> sortedChapters;    // danh sách sẽ chứa các chung đã được sắp xếp sau xử lý
                if (v.getId() == R.id.btn_filter_newest) {
                    sortedChapters = chapters.stream()  // tạo 1 stream từ danh sách cac chương
                            .sorted((o1, o2) -> {   // sắp xếp các phần tử trong stream
                                int num1 = Integer.parseInt(o1.replaceAll("[^0-9]", ""));   // loại bỏ cac ký tự không phải là số
                                int num2 = Integer.parseInt(o2.replaceAll("[^0-9]", ""));
                                return Integer.compare(num2, num1); // so sánh
                            })
                            .collect(Collectors.toList());   // thu thập các phần tử đã sap vào 1 danh sách
                    // cap nhật giao diện lọc mới
                    btnFilterNewest.setTextColor(Color.parseColor("#3CBEF5"));
                    btnFilterNewest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#3CBEF5")));
                    btnFilterOldest.setTextColor(Color.WHITE);
                    btnFilterOldest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                } else {
                    sortedChapters = chapters.stream()
                            .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.replaceAll("[^0-9]", ""))))
                            .collect(Collectors.toList());
                    btnFilterOldest.setTextColor(Color.parseColor("#3CBEF5"));
                    btnFilterOldest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#3CBEF5")));
                    btnFilterNewest.setTextColor(Color.WHITE);
                    btnFilterNewest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                }
                adapter.updateChapters(sortedChapters);
            }
        };
        btnFilterNewest.setOnClickListener(filterClickListener);
        btnFilterOldest.setOnClickListener(filterClickListener);
        // Hiển thị dialog
        dialog.show();
        // Gọi sự kiện click của btnFilterNewest để mặc định chọn lọc mới nhất
        dialog.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                btnFilterNewest.performClick();
            }
        });
        // Đặt chiều cao cụ thể cho dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getResources().getDisplayMetrics().heightPixels * 0.67));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
