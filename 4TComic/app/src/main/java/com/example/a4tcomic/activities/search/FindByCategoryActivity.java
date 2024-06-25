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
import com.example.a4tcomic.db.GenresDB;

public class FindByCategoryActivity extends AppCompatActivity {

    private TextView tv_writer, tv_category, tv_advanced;
    private EditText editSearch;
    private ImageButton btnSearch;
    private GenresDB genresDB;

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
        btnSearch = findViewById(R.id.btnSearch);
        editSearch = findViewById(R.id.editSearch);
        genresDB = new GenresDB();

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

        btnSearch.setOnClickListener(v -> {
            String genreName = editSearch.getText().toString().trim();

            if (!genreName.isEmpty()) {
                genresDB.getGenreIdByName(genreName, new GenresDB.GenreIdCallback() {
                    @Override
                    public void onGenreIdLoaded(String genreId) {
                        if (genreId != null) {
                            Intent intent = new Intent(FindByCategoryActivity.this, ListComicActivity.class);
                            intent.putExtra("genreId", genreId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(FindByCategoryActivity.this, "Không tìm thấy thể loại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(FindByCategoryActivity.this, "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
