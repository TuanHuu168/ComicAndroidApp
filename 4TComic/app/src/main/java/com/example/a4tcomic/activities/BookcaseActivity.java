package com.example.a4tcomic.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat.Type;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.BookAdapter;
import com.example.a4tcomic.models.ListBookCase;

import java.util.ArrayList;
import java.util.List;



public class BookcaseActivity extends AppCompatActivity implements View.OnTouchListener {
    ImageButton btnEdit, btnHomePage, btnArchive, btnSetting, btnNotification;
    Button btnFollow, btnChooseAll,btnHistory;
    EditText editSearch;
    RecyclerView rcvHistory;
    BookAdapter bookAdapter;
    LinearLayout llayout;
    boolean isEditButtonColored = false; // Trạng thái ban đầu của nút
    boolean isAllSelected = false; // Trạng thái ban đầu của nút

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookcase_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, ((Insets) systemBars).bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        editSearch = findViewById(R.id.editSearch);
        btnFollow = findViewById(R.id.btnFollow);
        btnEdit = findViewById(R.id.btnEdit);
        llayout = findViewById(R.id.llayout);
        llayout.setVisibility(View.GONE);
        btnChooseAll = findViewById(R.id.btnChooseAll);
        btnHistory = findViewById(R.id.btnHistory);

        btnHomePage = findViewById(R.id.btnHomePage);
        btnArchive = findViewById(R.id.btnArchive);
        btnNotification = findViewById(R.id.btnNotification);
        btnSetting = findViewById(R.id.btnSetting);

        btnHomePage.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, HomePageActivity.class);
            startActivity(settingIntent);
        });

        btnNotification.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, NotificationActivity.class);
            startActivity(settingIntent);
        });

        btnSetting.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, PersonalActivity.class);
            startActivity(settingIntent);
        });

        btnArchive.setOnClickListener(v -> {
            Intent archiveIntent = new Intent(this, BookcaseActivity.class);
            startActivity(archiveIntent);
        });

        // hide keyboard
        findViewById(R.id.main).setOnTouchListener(this);

        int blue_main = ContextCompat.getColor(this, R.color.blue_main);
        int white = ContextCompat.getColor(this, R.color.white);

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAdapter.setShowReadingChapter(true);
                btnHistory.setBackgroundColor(blue_main);
                btnFollow.setBackgroundColor(white);
                bookAdapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAdapter.setShowReadingChapter(false);
                btnFollow.setBackgroundColor(blue_main);
                btnHistory.setBackgroundColor(white);
                bookAdapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditButtonColored) {
                    btnEdit.clearColorFilter();
                    llayout.setVisibility(View.GONE);
                    // điều chỉnh radio
                    bookAdapter.setEditMode(false);
                } else {
                    btnEdit.setColorFilter(Color.parseColor("#3CBEF5"));
                    llayout.setVisibility(View.VISIBLE);
                    bookAdapter.setEditMode(true);
                }
                isEditButtonColored = !isEditButtonColored; // Đổi trạng thái
            }
        });

        btnChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllSelected){
                    bookAdapter.deselectAll();
                    btnChooseAll.setText("Chọn tất cả");
                }else{
                    btnChooseAll.setText("Bỏ chọn tất cả");
                    bookAdapter.selectAll();
                }
                isAllSelected=!isAllSelected; // đổi trạng thái
            }
        });
//        btnFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentFollow = new Intent(BookcaseActivity.this, activity_follow_page.class);
//                startActivity(intentFollow);
//            }
//        });

        rcvHistory = findViewById(R.id.rcvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        bookAdapter = new BookAdapter(getList(),false);
        rcvHistory.setAdapter(bookAdapter);

        // Set initial visibility state
        bookAdapter.setShowReadingChapter(true);
        bookAdapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
    }

    private List<ListBookCase> getList() {
        List<ListBookCase> list = new ArrayList<>();
        list.add(new ListBookCase(R.drawable.tinhtukiemsi, "Tinh tú kiếm sĩ"));
        list.add(new ListBookCase(R.drawable.ic_oce_piece, "ONE PIECE"));
        list.add(new ListBookCase(R.drawable.tinhtukiemsi, "Tinh tú kiếm sĩ 2"));
        list.add(new ListBookCase(R.drawable.ic_khong_chi_bat_nat, "Không chỉ là bắt nạt"));
        list.add(new ListBookCase(R.drawable.ic_khong_chi_bat_nat, "Không chỉ là bắt nạt"));
        list.add(new ListBookCase(R.drawable.ic_khong_chi_bat_nat, "Không chỉ là bắt nạt"));
        return list;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        editSearch.clearFocus();
        return false;
    }
}