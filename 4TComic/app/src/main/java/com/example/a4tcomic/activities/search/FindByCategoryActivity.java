package com.example.a4tcomic.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;

public class FindByCategoryActivity extends AppCompatActivity {

    private TextView tv_writer, tv_category, tv_advanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_by_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_writer = findViewById(R.id.tv_writer);
        tv_category = findViewById(R.id.tv_category);
        tv_advanced = findViewById(R.id.tv_advanced);

        tv_writer.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindByWriterActivity.class);
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
    }
}