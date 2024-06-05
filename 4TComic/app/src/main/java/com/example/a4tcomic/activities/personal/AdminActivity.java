package com.example.a4tcomic.activities.personal;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.CommentAdapter;
import com.example.a4tcomic.adapters.ContentAdapter;
import com.example.a4tcomic.models.CommentItem;
import com.example.a4tcomic.models.ContentItem;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    ImageButton btnBack;

    // Phần tài khoản mới tạo và truyện mới cập nhật
    private RecyclerView accountRecyclerView;
    private RecyclerView recentlyUpdatedRecyclerView;
    private List<ContentItem> accountList;
    private List<ContentItem> recentlyUpdatedList;

    // Phần bình luận
    private ListView commentListView;
    private CommentAdapter commentAdapter;
    private List<CommentItem> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        accountRecyclerView = findViewById(R.id.recycler_view_account);
        recentlyUpdatedRecyclerView = findViewById(R.id.recyclerViewRecentlyUpdated);
        commentListView = findViewById(R.id.list_view_comment);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> { finish(); });

        // Tạo custom listView
        accountList = new ArrayList<>();
        recentlyUpdatedList = new ArrayList<>();

        // Các tài khoản
        accountList.add(new ContentItem(1,R.drawable.truyen1, "Trịnh Khắc Tùng"));
        accountList.add(new ContentItem(1,R.drawable.truyen2, "Luyện Thị Ánh Tuyết"));
        accountList.add(new ContentItem(1,R.drawable.truyen4, "Nguyễn Tuấn Nghĩa"));

        // Thêm nội dung truyện mới tải lên
        recentlyUpdatedList.add(new ContentItem(2,R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!"));
        recentlyUpdatedList.add(new ContentItem(2,R.drawable.truyen2, "Tinh Tú Kiếm Sĩ"));
        recentlyUpdatedList.add(new ContentItem(2,R.drawable.truyen3, "Tôi Trở Nên Phi Thường Ngay Cả Ở Thế Giới Thật"));
        recentlyUpdatedList.add(new ContentItem(2,R.drawable.truyen4, "Không Chỉ Là Bắt Nạt"));

        // Bình luận
        commentList = new ArrayList<>();
        commentList.add(new CommentItem(R.drawable.truyen1, "Tiểu Thư Bé Bỏng Đáng Yêu!", "Tùng", "Ôi cô công chúa xinh quá. Ước gì mình cũng có cô con gái như vậy", "29 phút"));
        commentList.add(new CommentItem(R.drawable.truyen2, "Tinh Tú Kiếm Sĩ", "Tùng", "Lại thể loại truyện hack như mấy bộ ăn liền khác. Đọc chán thật đấy", "44 phút"));
        commentList.add(new CommentItem(R.drawable.truyen3, "Tôi Trở Nên Phi Thường Ngay Cả Ở Thế Giới Thật", "Tùng", "Lại mì ăn liền à", "52 phút"));


        // Thiết lập Adapter và LayoutManager cho RecyclerView
        commentAdapter = new CommentAdapter(this, R.layout.item_comment, commentList);
        commentListView.setAdapter(commentAdapter);

        // Thiết lập Adapter và LayoutManager cho RecyclerView
        setupRecyclerView(accountRecyclerView, accountList);
        setupRecyclerView(recentlyUpdatedRecyclerView, recentlyUpdatedList);

    }

    private void setupRecyclerView(RecyclerView recyclerView, List<ContentItem> itemList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ContentAdapter adapter = new ContentAdapter(this, itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}