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

public class FindByWriterActivity extends AppCompatActivity {

    private TextView tv_writer, tv_category, tv_advanced;
    private EditText editSearch;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_by_writer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_writer = findViewById(R.id.tv_writer);
        tv_category = findViewById(R.id.tv_category);
        tv_advanced = findViewById(R.id.tv_advanced);
        btnSearch = findViewById(R.id.btnSearch);
        editSearch = findViewById(R.id.edtSearch); // Corrected to editSearch based on your XML

        tv_category.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindByCategoryActivity.class);
            startActivity(intent);
            finish();
        });

        tv_advanced.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdvancedSearchActivity.class);
            startActivity(intent);
            finish();
        });

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            String writerName = editSearch.getText().toString().trim();
            if (!writerName.isEmpty()) {
                Intent intent = new Intent(this, ListComicActivity.class);
                intent.putExtra("writerName", writerName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(FindByWriterActivity.this, "Không tìm thấy tác giả", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
