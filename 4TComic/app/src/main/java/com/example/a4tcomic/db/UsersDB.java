package com.example.a4tcomic.db;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersDB {
    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mUsersRef;
    private final StorageReference mStorageRef;

    public UsersDB() {
        mDatabase = FirebaseDatabase.getInstance();
        mUsersRef = mDatabase.getReference("comic_db/users");
        mStorageRef = FirebaseStorage.getInstance().getReference("comic_db/avatar_users");
    }

    public interface UserCallback {
        void onUserLoaded(User user);
    }

    public interface UpdateUserCallback {
        void onSuccess();
    }

    public interface AllUsersCallback {
        void onAllUsersLoaded(List<User> users);
    }

    // lấy danh sách tất cả user
    public void getAllUsers(final AllUsersCallback callback) {
        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.add(user);
                }
                callback.onAllUsersLoaded(users);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // lấy user theo id
    public void getUserById(String userId, final UserCallback callback) {
        mUsersRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                callback.onUserLoaded(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // thêm user mới
    public void addUser(User user) {
        String userId = mUsersRef.push().getKey();
        user.setId(userId);
        mUsersRef.child(userId).setValue(user);
    }

    // update user
    public void updateUser(User user, String oldAvatarUrl, final UpdateUserCallback callback) {
        // user thay ảnh cũ thì sẽ xóa ảnh cũ trên storage
        if (oldAvatarUrl != "" && user.getAvatar_url() != oldAvatarUrl) {
            StorageReference oldAvatarRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldAvatarUrl);
            oldAvatarRef.delete().addOnCompleteListener(task -> {});
        }

        if (user.getAvatar_url() == "" || oldAvatarUrl == user.getAvatar_url()) {
            mUsersRef.child(user.getId()).setValue(user);
            callback.onSuccess();
        }
        else {
            updateAvatarAndUser(user, callback);
        }
    }

    // update user có thay ảnh mới
    public void updateAvatarAndUser(User user, final UpdateUserCallback callback) {
        StorageReference storageRef = mStorageRef.child(UUID.randomUUID().toString());
        UploadTask uploadTask = storageRef.putFile(Uri.parse(user.getAvatar_url()));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                user.setAvatar_url(uri.toString());
                mUsersRef.child(user.getId()).setValue(user);
                callback.onSuccess();
            });
        });
    }

    // admin khóa user
    public void lockUser(String userId) {
        mUsersRef.child(userId).child("status").setValue(1);
    }

    // admin xóa user thì sẽ xóa hẳn user
    public void deleteUserByAdmin(String userId) {
        mUsersRef.child(userId).removeValue();
    }

    // user tự xóa thì email và password user sẽ bị làm trống
    public void deleteUserByUser(String userId) {
        mUsersRef.child(userId).child("email").setValue("");
        mUsersRef.child(userId).child("password").setValue("");
    }

}