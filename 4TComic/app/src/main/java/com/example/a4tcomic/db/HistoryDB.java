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
import java.util.concurrent.atomic.AtomicInteger;

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
                        if (!snapshot.exists()) return;
                        List<History> historyUserList = new ArrayList<>();
                        for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                            History h = historySnapshot.getValue(History.class);
                            historyUserList.add(h);
                        }
                        historyUserList.sort((o1, o2) -> Long.compare(o2.getLast_date(), o1.getLast_date()));

                        ChaptersDB chaptersDB = new ChaptersDB();
                        ComicsDB comicsDB = new ComicsDB();

                        List<Chapter> chapters = new ArrayList<>();
                        List<Comic> comics = new ArrayList<>();

                        AtomicInteger ichap = new AtomicInteger(historyUserList.size());

                        for (History h : historyUserList) {
                            chaptersDB.getChapterById(h.getChapter_id(), chapter1 -> {
                                chapters.add(chapter1);
                                comicsDB.getComicById(chapter1.getComic_id(), comic1 -> {
                                    comics.add(comic1);

                                    ichap.getAndDecrement();
                                    if (ichap.get() == 0) {
                                        callback.onHistoryLoaded(historyUserList, comics, chapters);
                                    }
                                });
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    final String TAG = "HistoryDB";
    // thêm truyện vào danh sách lịch sử của user
    public void addComicReadByUser(Chapter curChapter, String user_id) {
        Log.d(TAG, "Đã có trong danh sách");
        mHistoryRef.orderByChild("user_id")
                .equalTo(user_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            addNewHistory(user_id, curChapter);
                            return;
                        }
                        List<History> historyUserList = new ArrayList<>();
                        for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                            History h = historySnapshot.getValue(History.class);
                            historyUserList.add(h);
                        }
                        historyUserList.sort((o1, o2) -> Long.compare(o2.getLast_date(), o1.getLast_date()));

                        ChaptersDB chaptersDB = new ChaptersDB();
                        List<Chapter> chapters = new ArrayList<>();
                        AtomicInteger ichap = new AtomicInteger(historyUserList.size());

                        for (History h : historyUserList) {
                            chaptersDB.getChapterById(h.getChapter_id(), chapter1 -> {
                                chapters.add(chapter1);
                                ichap.getAndDecrement();
                                if (ichap.get() == 0) {
                                    getComicsReadByUserDetail(user_id, curChapter, historyUserList, chapters);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void getComicsReadByUserDetail(String user_id, Chapter curChapter, List<History> history, List<Chapter> chapter)  {

        Long curTime = System.currentTimeMillis();

        for (int i = 0; i < chapter.size(); i++) {
            Chapter ch = chapter.get(i);
            if (ch.getId().equals(curChapter.getId())) {
                setChapter(curTime, history.get(i));
                return;
            }
        }
        for (int i = 0; i < chapter.size(); i++) {
            Chapter ch = chapter.get(i);
            if (ch.getComic_id().equals(curChapter.getComic_id())) {
                setComicChapter(curTime, history.get(i), curChapter);
                return;
            }
        }
        addNewHistory(user_id, curChapter);
    }

    private void addNewHistory(String user_id, Chapter curChapter) {
        // nếu chưa tồn tại thì thêm vào danh sách
        long curTime = System.currentTimeMillis();
        String id_h = mHistoryRef.push().getKey();
        History h = new History();
        h.setId(id_h);
        h.setUser_id(user_id);
        h.setChapter_id(curChapter.getId());
        h.setLast_date(curTime);

        mHistoryRef.child(id_h).setValue(h);
        Log.d(TAG, "Chưa xuất hiện trong lịch sử");
    }

    private void setChapter(long curTime, History h) {
        if ( (curTime - h.getLast_date()) > 600000){
            mHistoryRef.child(h.getId())
                    .child("last_date")
                    .setValue(System.currentTimeMillis());
            Log.d(TAG, "Chương đã có");
        }
        else {
            Log.d(TAG, "Chương đã có nhg time ngắn");
        }
    }

    private void setComicChapter(long curTime, History h, Chapter ch) {
        if ( (curTime - h.getLast_date()) > 100) {
            h.setLast_date(curTime);
            h.setChapter_id(ch.getId());
            mHistoryRef.child(h.getId()).setValue(h);
            Log.d(TAG, "Truyện đã có");
        } else {
            Log.d(TAG, "Truyện đã có nhg time ngắn");
        }
    }

    // xóa truyện khỏi danh sách đọc của user
    public void removeComicReadByUser(History history) {
        mHistoryRef.child(history.getId()).removeValue();
    }
}
