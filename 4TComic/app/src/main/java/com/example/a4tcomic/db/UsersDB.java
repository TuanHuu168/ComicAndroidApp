package com.example.a4tcomic.db;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.a4tcomic.models.User;
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
        mDatabase = FirebaseDatabase.getInstance("https://comic4t-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUsersRef = mDatabase.getReference("comic_db/users");
        mStorageRef = FirebaseStorage.getInstance("gs://comic4t.appspot.com").getReference().child("avatar_users");
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
        mUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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
        // Nếu có thay đổi ảnh đại diện, xóa ảnh cũ trên Firebase Storage
        if (oldAvatarUrl != null && !oldAvatarUrl.equals("") && !oldAvatarUrl.equals(user.getAvatar_url())) {
            StorageReference oldAvatarRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldAvatarUrl);
            oldAvatarRef.delete().addOnSuccessListener(aVoid -> {
                // Xóa thành công ảnh cũ, tiến hành upload ảnh mới và cập nhật thông tin user
                updateAvatarAndUser(user, callback);
            }).addOnFailureListener(e -> {
                // Xử lý lỗi nếu không thể xóa ảnh cũ
            });
        } else {
            // Không thay đổi ảnh đại diện, chỉ cập nhật thông tin user
            mUsersRef.child(user.getId()).setValue(user)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi nếu không thể cập nhật thông tin user
                    });
        }
    }

    // update user có thay đổi ảnh đại diện
    private void updateAvatarAndUser(User user, final UpdateUserCallback callback) {
        StorageReference storageRef = mStorageRef.child(UUID.randomUUID().toString());
        UploadTask uploadTask = storageRef.putFile(Uri.parse(user.getAvatar_url()));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Cập nhật URL mới cho ảnh đại diện và cập nhật thông tin user trên Realtime Database
                user.setAvatar_url(uri.toString());
                mUsersRef.child(user.getId()).setValue(user)
                        .addOnSuccessListener(aVoid -> callback.onSuccess())
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi nếu không thể cập nhật thông tin user
                        });
            });
        }).addOnFailureListener(e -> {
            // Xử lý lỗi nếu không thể upload ảnh mới
        });
    }

    // tìm userId bằng tên người dùng
    public void getUserIdByName(final String userName, final UsersDB.UserIdCallback callback) {
        mUsersRef.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        callback.onUserIdLoaded(userId);
                        return;
                    }
                }
                callback.onUserIdLoaded(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onUserIdLoaded(null);
            }
        });
    }

    // khóa user (ví dụ: status = 1 là bị khóa)
    public void lockUser(String userId) {
        mUsersRef.child(userId).child("status").setValue(1);
    }

    // xóa user bởi admin
    public void deleteUserByAdmin(String userId) {
        mUsersRef.child(userId).removeValue();
    }

    // xóa user tự do
    public void deleteUserByUser(String userId) {
        mUsersRef.child(userId).child("email").setValue("");
        mUsersRef.child(userId).child("password").setValue("");
    }

    // lấy tham chiếu đến bảng users
    public DatabaseReference getUsersRef() {
        return mUsersRef;
    }

    // lấy tham chiếu đến thư mục lưu trữ ảnh đại diện
    public StorageReference getStorageRef() {
        return mStorageRef;
    }

    public interface UserIdCallback {
        void onUserIdLoaded(String userId);
    }
}
