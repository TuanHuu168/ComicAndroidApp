package com.example.a4tcomic.db;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentsDB {
    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mCommentsRef;

    public CommentsDB() {
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mCommentsRef = mDatabase.getReference("comic_db/comments");
    }

    public interface CommentsCallback {
        void onCommentsLoaded(List<Comment> comments);
    }

    // lấy danh sách comment của truyện
    public void getComments(String comicId, CommentsCallback callback) {
        mCommentsRef.orderByChild("comic_id")
                .equalTo(comicId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Comment> comments = new ArrayList<>();
                        for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                            Comment comment = commentSnapshot.getValue(Comment.class);
                            comments.add(comment);
                        }
                        callback.onCommentsLoaded(comments);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    // thêm comment vào truyện
    public void addComment(Comment comment) {
        String key = mCommentsRef.push().getKey();
        comment.setId(key);
        mCommentsRef.child(key).setValue(comment);
    }

    // xóa comment khỏi truyện
    public void deleteComment(String commentId) {
        mCommentsRef.child(commentId).removeValue();
    }
}
