package com.example.a4tcomic.db;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.Genre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GenresDB {

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mGenresRef;
    private final DatabaseReference mComicGenreRef;

    public GenresDB() {
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mGenresRef = mDatabase.getReference("comic_db/genres");
        mComicGenreRef = mDatabase.getReference("comic_db/comic_genre");
    }

    public interface GenreCallback {
        void onGenres(List<Genre> genreList);
    }

    // lấy danh sách tất cả thể loại
    public void getAllGenres(final GenreCallback callback) {
        mGenresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Genre> comicGenreList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Genre child_genre = dataSnapshot.getValue(Genre.class);
                    if (child_genre != null)
                        comicGenreList.add(child_genre);
                }
                callback.onGenres(comicGenreList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // lấy danh sách thể loại theo truyện
    public void getGenresByComicId(String comic_id, final GenreCallback callback) {
        mComicGenreRef.orderByChild("comic_id")
                .equalTo(comic_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Genre> genreList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String genre_id = dataSnapshot.child("genre_id").getValue(String.class);
                            if (genre_id != null)

                                mGenresRef.child(genre_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String genre_name;
                                                if (snapshot.exists()) {
                                                    genre_name = snapshot.child("title")
                                                            .getValue(String.class);
                                                } else genre_name = "Unknown";
                                                Genre new_genre = new Genre(genre_id, genre_name);
                                                genreList.add(new_genre);
                                                callback.onGenres(genreList);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {}
                                        });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { return; }
                });
    }

    // thêm thể loại mới
    public void addGenre(String genre) {
        String key = mGenresRef.push().getKey();
        mGenresRef.child(key).setValue(genre);
        mGenresRef.child(key).child("id").setValue(key);
        mGenresRef.child(key).child("title").setValue(genre);
    }

    // thêm thể loại vào truyện
    public void addComicGenre(String comicId, String genreId) {
        String key = mComicGenreRef.push().getKey();
        mComicGenreRef.child(key).child("id").setValue(key);
        mComicGenreRef.child(key).child("genre_id").setValue(genreId);
        mComicGenreRef.child(key).child("comic_id").setValue(comicId);
    }

    public DatabaseReference getGenresRef() {
        return mGenresRef;
    }

    public DatabaseReference getComicGenreRef() {
        return mComicGenreRef;
    }
}
