package com.example.a4tcomic.db;

import android.util.Pair;

import androidx.annotation.NonNull;
import com.example.a4tcomic.models.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ComicsDB {

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mComicsRef;

    public ComicsDB() {
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mComicsRef = mDatabase.getReference("comic_db/comics");
    }

    public interface ComicCallback {
        void onComicLoaded(Comic comic);
    }

    public interface AllComicsCallback {
        void onAllComicsLoaded(List<Comic> comics);
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
//                comics.sort((o1, o2) -> Long.compare(o2.getCreated_at(), o1.getCreated_at()));
//                callback.onAllComicsLoaded(comics);
                fetchLatestChaptersAndSort(comics, callback);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void fetchLatestChaptersAndSort(List<Comic> comics, final AllComicsCallback callback) {
        ChaptersDB chaptersDB = new ChaptersDB();
        List<Pair<Comic, Long>> comicsWithLatestChapterTime = new ArrayList<>();

        for (Comic comic : comics) {
            chaptersDB.getChapters(comic.getId(), new ChaptersDB.ChaptersCallback() {
                @Override
                public void onChaptersLoaded(List<Chapter> chapters) {
                    long latestChapterTime = 0;
                    if (!chapters.isEmpty()) {
                        latestChapterTime = chapters.get(0).getCreated_at(); // Chương đầu tiên là chương mới nhất
                    }
                    comicsWithLatestChapterTime.add(new Pair<>(comic, latestChapterTime));

                    if (comicsWithLatestChapterTime.size() == comics.size()) {
                        // Đảm bảo đã lấy được thời gian chương mới nhất cho tất cả truyện
                        Collections.sort(comicsWithLatestChapterTime, (o1, o2) -> Long.compare(o2.second, o1.second));

                        // Tạo danh sách comic đã sắp xếp
                        List<Comic> sortedComics = new ArrayList<>();
                        for (Pair<Comic, Long> pair : comicsWithLatestChapterTime) {
                            sortedComics.add(pair.first);
                        }

                        callback.onAllComicsLoaded(sortedComics);
                    }
                }
            });
        }
    }



    // tìm truyện theo id
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

    // thêm truyện mới
    public void addComic(Comic comic) {
        String key = mComicsRef.push().getKey();
        comic.setId(key);
        mComicsRef.child(key).setValue(comic);
    }

    // xóa truyện theo id
    public void deleteComic(String comic_id) {
        mComicsRef.child(comic_id).removeValue();
    }

    // lấy truyện theo author_id
    public void getComicsByAuthorId(String author_id, final AllComicsCallback callback) {
        mComicsRef.orderByChild("author_id")
                .equalTo(author_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Comic> comics = new ArrayList<>();
                        for (DataSnapshot comicSnapshot : snapshot.getChildren()) {
                            Comic comic = comicSnapshot.getValue(Comic.class);
                            comics.add(comic);
                        }
                        comics.sort((o1, o2) -> Long.compare(o2.getCreated_at(), o1.getCreated_at()));
                        callback.onAllComicsLoaded(comics);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    // lấy truyện theo user_id người dịch
    public void getComicByUserId(String user_id, final AllComicsCallback callback) {
        mComicsRef.orderByChild("user_id").equalTo(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comic> comics = new ArrayList<>();
                for (DataSnapshot comicSnapshot : snapshot.getChildren()) {
                    Comic comic = comicSnapshot.getValue(Comic.class);
                    comics.add(comic);
                }
                comics.sort((o1, o2) -> Long.compare(o2.getCreated_at(), o1.getCreated_at()));
                callback.onAllComicsLoaded(comics);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // lấy truyện theo genre_id
    public void getComicsByGenreId(String genre_id, final AllComicsCallback callback) {
        mComicsRef.orderByChild("genre_id").equalTo(genre_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comic> comics = new ArrayList<>();
                for (DataSnapshot comicSnapshot : snapshot.getChildren()) {
                    Comic comic = comicSnapshot.getValue(Comic.class);
                    comics.add(comic);
                }
                comics.sort((o1, o2) -> Long.compare(o2.getCreated_at(), o1.getCreated_at()));
                callback.onAllComicsLoaded(comics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
