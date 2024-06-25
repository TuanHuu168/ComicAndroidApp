package com.example.a4tcomic.utilities;

import com.example.a4tcomic.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserUtil {

    public interface OnUserFetchListener {
        void onUserFetched(String username);
        void onUserFetchFailed(String errorMessage);
    }

    public static void fetchUsername(String userId, final OnUserFetchListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        listener.onUserFetched(user.getUsername());
                    } else {
                        listener.onUserFetchFailed("User data is null");
                    }
                } else {
                    listener.onUserFetchFailed("User does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onUserFetchFailed(databaseError.getMessage());
            }
        });
    }
}
