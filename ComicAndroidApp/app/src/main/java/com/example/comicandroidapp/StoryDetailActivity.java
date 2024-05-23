package com.example.comicandroidapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoryDetailActivity extends AppCompatActivity {

    private TextView des_tv;
    private Button btn_favorite;
    private boolean isExpanded = false;
    private boolean isFavorite = false;

    private List<List<String>> chapterGroups;
    private ChapterPagerAdapter pagerAdapter;
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

        des_tv = findViewById(R.id.des_tv);
        btn_favorite = findViewById(R.id.btn_favorite);

        btn_favorite.setOnClickListener(v -> {
            if (!isFavorite) {
                btn_favorite.setText("Đã thích");
                btn_favorite.setBackgroundResource(R.drawable.btn_favorited);
                btn_favorite.setTextColor(ContextCompat.getColor(this,R.color.pink_main));
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited, 0, 0, 0);
                isFavorite = true;
            } else {
                btn_favorite.setText("Yêu thích");
                btn_favorite.setBackgroundResource(R.drawable.btn_favorite);
                btn_favorite.setTextColor(ContextCompat.getColor(this,R.color.white));
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_white, 0, 0, 0);
                isFavorite = false;
            }
        });

        String description = "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm - Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu" +
                "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm - Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu" +
                "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm - Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu" +
                "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm - Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy. Cậu";
        des_tv.setText(description);

        des_tv.setOnClickListener(v -> {
            if (!isExpanded) {
                des_tv.setMaxLines(Integer.MAX_VALUE); // Hiển thị toàn bộ văn bản
                des_tv.setEllipsize(null);
                des_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.up);
                isExpanded = true;
            } else if (isExpanded) {
                des_tv.setMaxLines(3); // Thu gọn lại 3 dòng
                des_tv.setEllipsize(TextUtils.TruncateAt.END);
                des_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.down);
                isExpanded = false;
            }
        });

        // Initialize chapter groups
        chapterGroups = new ArrayList<>();
        chapterGroups.add(Arrays.asList("Chương 1", "Chương 2", "Chương 3"));
        chapterGroups.add(Arrays.asList("Chương 51", "Chương 52", "Chương 53"));
        chapterGroups.add(Arrays.asList("Chương 101", "Chương 102", "Chương 103"));
        // Add more groups as needed

        // Set up ViewPager and TabLayout
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new ChapterPagerAdapter(this, chapterGroups);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("1-50");
                    break;
                case 1:
                    tab.setText("51-100");
                    break;
                case 2:
                    tab.setText("101-150");
                    break;
                // Add more cases as needed
            }
        }).attach();
    }



}