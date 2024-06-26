package com.example.a4tcomic.activities.story;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.CommentAdapter;
import com.example.a4tcomic.db.CommentsDB;
import com.example.a4tcomic.models.Comment;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private EditText edtComment;
    private TextView tvNumberComment;
    private Button btnSubmitComment;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private CommentsDB commentsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Ánh xạ
        edtComment = findViewById(R.id.edtComment);
        tvNumberComment = findViewById(R.id.tvNumberComment);
        btnSubmitComment = findViewById(R.id.btSubmitComment);
        recyclerView = findViewById(R.id.recyclerComment);
        commentsDB = new CommentsDB();

        // Khởi tạo danh sách và adapter
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this);

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        // Nhận Intent để lấy comicId
        String comicId = getIntent().getStringExtra("comicId");

        // Sự kiện nút gửi comment
        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = edtComment.getText().toString().trim();
                long timestamp = System.currentTimeMillis();
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                String userId = sharedPreferences.getString("id", "");

                if (!commentContent.isEmpty()) {
                    Comment comment = new Comment("", commentContent, comicId, userId, timestamp);
                    commentsDB.addComment(comment);
                    Toast.makeText(CommentActivity.this, "Đã đăng comment thành công", Toast.LENGTH_SHORT).show();
                    edtComment.setText("");
                } else {
                    Toast.makeText(CommentActivity.this, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load comments
        commentAdapter.loadComments(comicId);

        // Hiển thị số lượng comment
        commentsDB.getComments(comicId, new CommentsDB.CommentsCallback() {
            @Override
            public void onCommentsLoaded(List<Comment> comments) {
                commentList.clear();
                commentList.addAll(comments);
                commentAdapter.notifyDataSetChanged();
                tvNumberComment.setText(comments.size() + " comments");
            }
        });
    }
}