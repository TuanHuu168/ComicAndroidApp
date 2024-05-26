package com.example.a4tcomic.personal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity{

    private EditText et_name;
    private ImageButton btn_return;
    private RecyclerView rcv_avatar;
    private AvatarAdapter avatarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_name = findViewById(R.id.et_name);
        btn_return = findViewById(R.id.btn_return);
        rcv_avatar = findViewById(R.id.rcv_avatars);

        // Trở lại trang trước
        btn_return.setOnClickListener(v -> finish());

        // Hiển thị list avatar
        avatarAdapter = new AvatarAdapter();
        GridLayoutManager gLM = new GridLayoutManager(this, 4);
        rcv_avatar.setLayoutManager(gLM);
        rcv_avatar.setFocusable(false);
        rcv_avatar.setNestedScrollingEnabled(false);
        avatarAdapter.setData(getAvatarList());
        rcv_avatar.setAdapter(avatarAdapter);

    }

    private List<Integer> getAvatarList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            list.add(R.drawable.avatar);
        }
        return list;
    }

}