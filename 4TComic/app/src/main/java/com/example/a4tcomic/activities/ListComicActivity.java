package com.example.a4tcomic.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.ComicAdapter;
import com.example.a4tcomic.models.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListComicActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private ListView listView;
    private ComicAdapter comicAdapter;
    private ArrayList<Comic> comicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comic);

        btn_back = findViewById(R.id.btn_back);
        listView = findViewById(R.id.lvComicSearch);
        comicList = new ArrayList<>();
        comicAdapter = new ComicAdapter(this, comicList);
        listView.setAdapter(comicAdapter);

        btn_back.setOnClickListener(v -> finish());

        // Get writerName from intent extra
        String writerName = getIntent().getStringExtra("writerName");

        // Query to find authorId based on writerName
        DatabaseReference authorsRef = FirebaseDatabase.getInstance().getReference().child("authors");
        Query query = authorsRef.orderByChild("name").equalTo(writerName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String authorId = snapshot.getKey(); // Get authorId ("-O09Z-TJsY5B6L86Xjha")
                        // Proceed to query comics with this authorId
                        queryComics(authorId);
                    }
                } else {
                    // Handle case where author with writerName does not exist
                    Log.d("ListComicActivity", "No author found with name: " + writerName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ListComicActivity", "Error querying author: " + databaseError.getMessage());
            }
        });
    }

    private void queryComics(String authorId) {
        DatabaseReference comicsRef = FirebaseDatabase.getInstance().getReference().child("comics");
        Query query = comicsRef.orderByChild("author_id").equalTo(authorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comicList.clear(); // Clear existing list
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Comic comic = snapshot.getValue(Comic.class);
                        if (comic != null) {
                            comicList.add(comic);
                        }
                    }
                    comicAdapter.notifyDataSetChanged(); // Notify adapter of data change
                } else {
                    // Handle case where no comics are found for the authorId
                    Log.d("ListComicActivity", "No comics found for authorId: " + authorId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ListComicActivity", "Error querying comics: " + databaseError.getMessage());
            }
        });
    }
}
