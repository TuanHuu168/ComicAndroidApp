package com.example.a4tcomic.db;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.Chapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChaptersDB {

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mChaptersRef;

    public ChaptersDB() {
        mDatabase = FirebaseDatabase.getInstance();
        mChaptersRef = mDatabase.getReference("comic_db/chapters");
    }

    public interface ChaptersCallback {
        void onChaptersLoaded(List<Chapter> chapters);
    }

    public interface ChapterCallback {
        void onChapterLoaded(Chapter chapter);
    }

    public void getChapters(String comic_id, ChaptersCallback callback) {
        mChaptersRef.orderByChild("comic_id")
                .equalTo(comic_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Chapter> chapterList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Chapter chapter = dataSnapshot.getValue(Chapter.class);
                            chapterList.add(chapter);
                        }
                        // sắp xếp chapter trong chapters theo order giảm dần
                        chapterList.sort((o1, o2) -> Integer.compare(o2.getOrder(), o1.getOrder()));
                        callback.onChaptersLoaded(chapterList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    public void getChapterById(String chapter_id, final ChapterCallback callback) {
        mChaptersRef.child(chapter_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chapter chapter = snapshot.getValue(Chapter.class);
                callback.onChapterLoaded(chapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void addChapter(Chapter chapter) {
        String key = mChaptersRef.push().getKey();
        mChaptersRef.child(key).setValue(chapter);
    }

    public void updateChapter(Chapter chapter) {
        mChaptersRef.child(chapter.getId()).setValue(chapter);
    }

    public void deleteChapter(Chapter chapter) {
        mChaptersRef.child(chapter.getId()).removeValue();
    }

    public DatabaseReference getChaptersRef() {
        return mChaptersRef;
    }
}
