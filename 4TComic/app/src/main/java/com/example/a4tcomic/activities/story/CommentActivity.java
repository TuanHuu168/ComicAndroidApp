package com.example.a4tcomic.activities.story;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.CommentAdapter;
import com.example.a4tcomic.db.CommentsDB;
import com.example.a4tcomic.models.Comment;
import com.example.a4tcomic.models.CommentItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {

    private EditText edtComment;
    private Button btnSubmitComment;
    private ImageButton btn_back;
    private CommentsDB  commentsDB;
    private DatabaseReference commentsRef;
    private TextView tvNumberComment;

    private ListView listViewComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        commentsRef = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("comic_db").child("comments");

        listViewComments = findViewById(R.id.lvComment);
        edtComment = findViewById(R.id.edtComment);
        btnSubmitComment = findViewById(R.id.btSubmitComment);
        tvNumberComment = findViewById(R.id.tvNumberComment);

        // Initialize adapter and set it to ListView
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, R.layout.item_comment_2, commentList);
        listViewComments.setAdapter(commentAdapter);


        // Submit comment button click listener
        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentId = commentsRef.push().getKey();
                String commentContent = edtComment.getText().toString().trim();
                long timestamp = System.currentTimeMillis();
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                String userId = sharedPreferences.getString("id", "");

                Comment comment = new Comment(commentId, commentContent, "comic001", userId, timestamp);
                commentsRef.child(commentId).setValue(comment)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(CommentActivity.this, "Đã đăng comment thành công", Toast.LENGTH_SHORT).show();
                            edtComment.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(CommentActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Display number of comments
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("CommentActivity", "onDataChange: Total comments count = " + snapshot.getChildrenCount());

                // Clear previous list and add new comments
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();

                // Update comment count text view
                long commentCount = snapshot.getChildrenCount();
                tvNumberComment.setText(commentCount + " comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CommentActivity", "onCancelled: " + error.getMessage());
            }
        });
    }
}

