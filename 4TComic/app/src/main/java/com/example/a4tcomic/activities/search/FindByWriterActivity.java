package com.example.a4tcomic.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a4tcomic.R;
import com.example.a4tcomic.db.AuthorsDB;

public class FindByWriterActivity extends AppCompatActivity {

    private TextView tv_writer, tv_category, tv_advanced;
    private EditText editSearch;
    private ImageButton btnSearch;
    private AuthorsDB authorsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_writer);

        tv_writer = findViewById(R.id.tv_writer);
        tv_category = findViewById(R.id.tv_category);
        tv_advanced = findViewById(R.id.tv_advanced);
        btnSearch = findViewById(R.id.btnSearch);
        editSearch = findViewById(R.id.edtSearch);
        authorsDB = new AuthorsDB();

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
                authorsDB.getAuthorIdByName(writerName, new AuthorsDB.AuthorIdCallback() {
                    @Override
                    public void onAuthorIdLoaded(String authorId) {
                        if (authorId != null) {
                            Intent intent = new Intent(FindByWriterActivity.this, ListComicActivity.class);
                            intent.putExtra("authorId", authorId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(FindByWriterActivity.this, R.string.toast_author_not_found, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(FindByWriterActivity.this, R.string.toast_please_enter_author, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
