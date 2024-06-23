package com.example.a4tcomic.db;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.Favorite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComicsDB {

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mComicsRef;
    private final DatabaseReference mFavoritesRef;

    public ComicsDB() {
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mComicsRef = mDatabase.getReference("comic_db/comics");
        mFavoritesRef = mDatabase.getReference("comic_db/favorites");
    }

    public interface ComicCallback {
        void onComicLoaded(Comic comic);
    }

    public interface AllComicsCallback {
        void onAllComicsLoaded(List<Comic> comics);
    }

    public interface UserFavorComicsCallback {
        void onUserFavorComicsLoaded(List<Comic> comics);
    }

    // lấy danh sách tất cả truyện
    public void getAllComics(final AllComicsCallback callback) {
        mComicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comic> comics = new ArrayList<>();
                for (DataSnapshot comicSnapshot : snapshot.getChildren()) {
                    Comic comic = comicSnapshot.getValue(Comic.class);
                    comics.add(comic);
                }
                callback.onAllComicsLoaded(comics);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // lấy truyện theo id
    public void getComicById(String comic_id, final ComicCallback callback) {
        mComicsRef.child(comic_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Comic comic = snapshot.getValue(Comic.class);
                        callback.onComicLoaded(comic);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    // lấy danh sách truyện yêu thích theo user_id
    // sắp xếp theo created_at giảm dần
    public void getComicsFavorByUser(String user_id, final UserFavorComicsCallback callback) {
        mFavoritesRef.orderByChild("user_id")
                .equalTo(user_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Favorite> favoritesUserList = new ArrayList<>();
                        for (DataSnapshot favorSnapshot : snapshot.getChildren()) {
                            Favorite favorite = favorSnapshot.getValue(Favorite.class);
                            favoritesUserList.add(favorite);
                        }
                        favoritesUserList.sort((o1, o2) -> Integer.compare(o2.getCreated_at(), o1.getCreated_at()));

                        List<Comic> comics = new ArrayList<>();
                        for (Favorite favorite : favoritesUserList) {
                            getComicById(favorite.getComic_id(), comic ->
                                    comics.add(comic));
                        }
                        callback.onUserFavorComicsLoaded(comics);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    public void addComic(Comic comic) {
        String key = mComicsRef.push().getKey();
        comic.setId(key);
        mComicsRef.child(key).setValue(comic);
    }

    public void deleteComic(Comic comic) {
        mComicsRef.child(comic.getId()).removeValue();
    }

    public DatabaseReference getComicsRef() {
        return mComicsRef;
    }
}
