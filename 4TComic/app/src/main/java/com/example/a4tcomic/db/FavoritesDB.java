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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesDB {
    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mFavoritesRef;

    public FavoritesDB() {
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFavoritesRef = mDatabase.getReference("comic_db/favorites");
    }

    public interface FavoriteCallback {
        void onFavoriteLoaded(String id);
    }

    public interface FavoriteComicsCallback {
        void onFavoriteComicsLoaded(List<Favorite> favorites, List<Comic> comics);
    }

    public void getFavorite(String user_id, String comic_id, final FavoriteCallback callback) {
        mFavoritesRef
                .orderByChild("user_id")
                .equalTo(user_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String comicId = dataSnapshot.child("comic_id").getValue(String.class);
                            if (comicId != null && comicId.equals(comic_id)) {
                                callback.onFavoriteLoaded(""+dataSnapshot.getKey());
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    // lấy danh sách truyện yêu thích theo user_id
    // sắp xếp theo created_at giảm dần
    public void getComicsFavorByUser(String user_id, final FavoriteComicsCallback callback) {
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

                        ComicsDB comicsDB = new ComicsDB();
                        List<Comic> comics = new ArrayList<>();
                        for (Favorite favorite : favoritesUserList) {
                            comicsDB.getComicById(favorite.getComic_id(), comic -> {
                                comics.add(comic);
                                callback.onFavoriteComicsLoaded(favoritesUserList, comics);
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    // lấy danh sách truyện được yêu thích nhiều nhất
    public void getComicsMostFavor(final ComicsDB.AllComicsCallback callback) {
        mFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Integer> comicCounts = new HashMap<>();

                for (DataSnapshot favorSnapshot : snapshot.getChildren()) {
                    String comic_id = favorSnapshot.child("comic_id").getValue(String.class);
                    if (comic_id != null) {
                        comicCounts.put(comic_id, comicCounts.getOrDefault(comic_id, 0) + 1);
                    }
                }

                List<Map.Entry<String, Integer>> sortedComics = new ArrayList<>(comicCounts.entrySet());
                sortedComics.sort((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));

                ComicsDB comicsDB = new ComicsDB();
                List<Comic> comics = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : sortedComics) {
                    comicsDB.getComicById(entry.getKey(), comic ->
                            comics.add(comic));
                }
                callback.onAllComicsLoaded(comics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void addFavorite(String comicId, String userId, long created_at) {
        String key = mFavoritesRef.push().getKey();
        mFavoritesRef.child(key).child("id").setValue(key);
        mFavoritesRef.child(key).child("comic_id").setValue(comicId);
        mFavoritesRef.child(key).child("user_id").setValue(userId);
        mFavoritesRef.child(key).child("created_at").setValue(created_at);
    }

    public void removeFavorite(String id) {
        mFavoritesRef.child(id).removeValue();
    }

    public DatabaseReference getFavoritesRef() {
        return mFavoritesRef;
    }
}
