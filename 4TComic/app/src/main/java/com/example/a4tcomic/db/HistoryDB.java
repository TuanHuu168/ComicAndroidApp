package com.example.a4tcomic.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.Chapter;
import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.History;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryDB {
    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mHistoryRef;

    public HistoryDB() {
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mHistoryRef = mDatabase.getReference("comic_db/history");
    }

    public interface HistoryCallback {
        void onHistoryLoaded(List<History> history, List<Comic> comic, List<Chapter> chapter);
    }

    // lấy danh sách lịch sử đọc của user
    // sắp xếp theo thời gian đọc gần nhất
    // lấy danh sách lịch sử, truyện, chương
    public void getComicsReadByUser(String user_id, final HistoryCallback callback) {
        mHistoryRef.orderByChild("user_id")
                .equalTo(user_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<History> historyUserList = new ArrayList<>();
                        for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                            History history = historySnapshot.getValue(History.class);
                            historyUserList.add(history);
                        }
                        historyUserList.sort((o1, o2) -> Integer.compare(o2.getLast_date(), o1.getLast_date()));

                        ChaptersDB chaptersDB = new ChaptersDB();
                        ComicsDB comicsDB = new ComicsDB();

                        List<Chapter> chapters = new ArrayList<>();
                        List<Comic> comics = new ArrayList<>();

                        for (History h : historyUserList) {
                            chaptersDB.getChapterById(h.getChapter_id(), chapter1 -> {
                                chapters.add(chapter1);
                                comicsDB.getComicById(chapter1.getComic_id(), comic1 -> {
                                    comics.add(comic1);
                                    callback.onHistoryLoaded(historyUserList, comics, chapters);
                                });
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    // thêm truyện vào danh sách đọc của user
    public void addComicReadByUser(History history) {
        String key = mHistoryRef.push().getKey();
        history.setId(key);
        mHistoryRef.child(key).setValue(history);
    }

    // xóa truyện khỏi danh sách đọc của user
    public void removeComicReadByUser(History history) {
        mHistoryRef.child(history.getId()).removeValue();
    }
}
