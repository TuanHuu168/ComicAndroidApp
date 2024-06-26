package com.example.a4tcomic.activities.admin;

import android.os.Bundle;
import android.widget.Button;
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
import com.example.a4tcomic.models.CommentItem;
import com.example.a4tcomic.models.ContentItem;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnAccount, btnUpdated, btnComment;

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

        btnBack = findViewById(R.id.btnBack);
        btnAccount = findViewById(R.id.btnAccount);
        btnUpdated = findViewById(R.id.btnUpdated);
        btnComment = findViewById(R.id.btnComment);

        btnBack.setOnClickListener(v -> finish());

    }

    private void setupRecyclerView(RecyclerView recyclerView, List<ContentItem> itemList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //ContentAdapter adapter = new ContentAdapter(this, itemList);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(adapter);
    }

}