package com.example.a4tcomic.db;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.Author;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AuthorsDB {

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mAuthorsRef;

    public AuthorsDB() {
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mAuthorsRef = mDatabase.getReference("comic_db/authors");
    }

    public interface AllAuthorsCallback {
        void onAllAuthorsLoaded(List<Author> authors);
    }

    public interface AuthorCallback {
        void onAuthorLoaded(Author author);
    }

    // lấy tất cả tác giả
    public void getAllAuthors(final AllAuthorsCallback callback) {
        mAuthorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Author> authors = new ArrayList<>();
                for (DataSnapshot authorSnapshot : snapshot.getChildren()) {
                    Author author = authorSnapshot.getValue(Author.class);
                    authors.add(author);
                }
                callback.onAllAuthorsLoaded(authors);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // lấy tên tác giả theo author_id trong truyện
    public void getAuthor(String id, AuthorCallback callback) {
        mAuthorsRef.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String author_name = "";
                        if (snapshot.exists()) {
                            author_name = snapshot.child("name")
                                    .getValue(String.class);
                        } else author_name = "Unknown";
                        Author new_author = new Author(id, author_name);
                        callback.onAuthorLoaded(new_author);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onAuthorLoaded(new Author("", "Unknown"));
                    }
                });
    }

    // thêm tác giả
    public void addAuthor(String name) {
        String key = mAuthorsRef.push().getKey();
        mAuthorsRef.child(key).child("id").setValue(key);
        mAuthorsRef.child(key).child("name").setValue(name);
    }

    public DatabaseReference getAuthorsRef() {
        return mAuthorsRef;
    }
}
