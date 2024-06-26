package com.example.a4tcomic.db;

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
                        historyUserList.sort((o1, o2) -> Long.compare(o2.getLast_date(), o1.getLast_date()));

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
    public void addComicReadByUser(String comic_id, Chapter curChapter, String user_id) {
        getComicsReadByUser(user_id, (history, comic, chapter) -> {
            boolean isComicExist = false;
            boolean isChapterExist = false;
            int pos = -1;

            // kiểm tra chương / truyện đã tồn tại hay chưa
            // nếu có thì set True và lấy vị trí đó
            for (int i = 0; i < chapter.size(); i++) {
                Chapter ch = chapter.get(i);
                if (ch.getId().equals(curChapter.getId())) {
                    isChapterExist = true;
                    pos = i;
                    break;
                }
                else if (ch.getComic_id().equals(comic_id)) {
                    isComicExist = true;
                    break;
                }
            }

            // nếu chưa tồn tại thì thêm vào danh sách
            if (pos == -1) {
                String id_h = mHistoryRef.push().getKey();
                History h = new History();
                h.setId(id_h);
                h.setUser_id(user_id);
                h.setChapter_id(curChapter.getId());
                h.setLast_date(System.currentTimeMillis());
                mHistoryRef.child(id_h).setValue(h);
                return;
            }

            Chapter ch = chapter.get(pos);
            Long curTime = System.currentTimeMillis();
            String id_h = history.get(pos).getId();

            // nếu chương có trong lịch sử và thời gian đọc gần hơn 5 phút
            // cập nhật thời gian đọc
            if (isChapterExist &&
                    (curTime - history.get(pos).getLast_date()) > 300000 )
            {
                mHistoryRef.child(id_h)
                        .child("last_date")
                        .setValue(System.currentTimeMillis());
                return;
            }

            // nếu truyện có trong lịch sử
            // cập nhật chương đọc gần nhất và thời gian đọc
            if (isComicExist) {
                mHistoryRef.child(id_h)
                        .child("last_date")
                        .setValue(curTime);
                mHistoryRef.child(id_h)
                        .child("chapter_id")
                        .setValue(curChapter.getId());
            }
        });
    }

    // xóa truyện khỏi danh sách đọc của user
    public void removeComicReadByUser(History history) {
        mHistoryRef.child(history.getId()).removeValue();
    }
}
