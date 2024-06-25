package com.example.a4tcomic.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;
import com.example.a4tcomic.db.UsersDB;

public class AdvancedSearchActivity extends AppCompatActivity {

    private TextView tv_writer, tv_category, tv_advanced;
    private EditText editSearch;
    private ImageButton btnSearch;
    private UsersDB usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advanced_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_writer = findViewById(R.id.tv_writer);
        tv_category = findViewById(R.id.tv_category);
        tv_advanced = findViewById(R.id.tv_advanced);
        btnSearch = findViewById(R.id.btnSearch);
        editSearch = findViewById(R.id.edtSearch);
        usersDB = new UsersDB();

        tv_writer.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindByWriterActivity.class);
            startActivity(intent);
            finish();
        });

        tv_category.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindByCategoryActivity.class);
            startActivity(intent);
            finish();
        });

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            String userName = editSearch.getText().toString().trim();

            if (!userName.isEmpty()) {
                usersDB.getUserIdByName(userName, new UsersDB.UserIdCallback() {
                    @Override
                    public void onUserIdLoaded(String userId) {
                        if (userId != null) {
                            Intent intent = new Intent(AdvancedSearchActivity.this, ListComicActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdvancedSearchActivity.this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(AdvancedSearchActivity.this, "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}