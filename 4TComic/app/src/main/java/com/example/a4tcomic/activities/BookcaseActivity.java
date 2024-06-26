package com.example.a4tcomic.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat.Type;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.FollowAdapter;
import com.example.a4tcomic.adapters.HistoryAdapter;
import com.example.a4tcomic.db.ChaptersDB;
import com.example.a4tcomic.db.ComicsDB;
import com.example.a4tcomic.db.HistoryDB;
import com.example.a4tcomic.models.Chapter;
import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.History;
import com.example.a4tcomic.models.ListBookCase;

import java.util.ArrayList;
import java.util.List;

public class BookcaseActivity extends AppCompatActivity implements View.OnTouchListener {
    ImageButton btnEdit, btnHomePage, btnArchive, btnSetting, btnNotification;
    Button btnChooseAll, btnDelete, btnChooseAllFollow, btnDeleteFollow;
    EditText editSearch, editSearchFollow;
    RecyclerView rcvHistory, rcvFollow;
    HistoryAdapter historyAdapter;
    FollowAdapter followAdapter;
    LinearLayout llayout, llayoutFollow;
    boolean isEditButtonColored = false; // Trạng thái ban đầu của nút
    boolean isAllSelected = false; // Trạng thái ban đầu của nút

    private HistoryDB historyDB;
    private String user_id;

    // Thiết lập tabLayout
    TabHost mytab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookcase_page);

        // Xử lý tabHost
        mytab = findViewById(R.id.mytabhost);
        mytab.setup();
        // khai báo các Tab con (TabSpec)
        TabHost.TabSpec spec1, spec2;
        // ứng với mỗi tab con, thực hiện 4 công việc
        // lịch sử
        spec1 = mytab.newTabSpec("lichsu");   // tạo mới tab
        spec1.setContent(R.id.tabHistory); // tham chiếu id tab con
        spec1.setIndicator("Lịch sử");
        mytab.addTab(spec1);
        // theo dõi
        spec2 = mytab.newTabSpec("yeuthich");   // tạo mới tab
        spec2.setContent(R.id.tabFollow); // tham chiếu id tab con
        spec2.setIndicator("Yêu thích");
        mytab.addTab(spec2);

        // setup edit text search
        findViewById(R.id.main).setOnTouchListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        editSearch = findViewById(R.id.editSearch);
        editSearchFollow = findViewById(R.id.editSearchFollow);
        btnEdit = findViewById(R.id.btnEdit);
        llayout = findViewById(R.id.llayout);
        llayout.setVisibility(View.GONE);
        llayoutFollow = findViewById(R.id.llayoutFollow);
        llayoutFollow.setVisibility(View.GONE);
        btnChooseAll = findViewById(R.id.btnChooseAll);
        btnChooseAllFollow = findViewById(R.id.btnChooseAllFollow);
        btnDelete = findViewById(R.id.btnDelete);
        btnDeleteFollow = findViewById(R.id.btnDeleteFollow);
        btnHomePage = findViewById(R.id.btnHomePage);
        btnArchive = findViewById(R.id.btnArchive);
        btnNotification = findViewById(R.id.btnNotification);
        btnSetting = findViewById(R.id.btnSetting);

        btnHomePage.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, HomePageActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnNotification.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, NotificationActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnSetting.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, PersonalActivity.class);
            startActivity(settingIntent);
            finish();
        });

        // hide keyboard
        findViewById(R.id.main).setOnTouchListener(this);

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(BookcaseActivity.this)
                    .setTitle(R.string.confirm_delete_history)
                    .setMessage(R.string.confirm_delete_history_message)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Xóa đơn vị khỏi SQLite
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        btnDeleteFollow.setOnClickListener(v -> {
            new AlertDialog.Builder(BookcaseActivity.this)
                    .setTitle(R.string.confirm_delete_follow)
                    .setMessage(R.string.confirm_delete_follow_message)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Xóa đơn vị khỏi SQLite
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        btnEdit.setOnClickListener(v -> {
            if (isEditButtonColored) {
                btnEdit.clearColorFilter();
                llayout.setVisibility(View.GONE);
                llayoutFollow.setVisibility(View.GONE);
                // Tắt chế độ chỉnh sửa
                historyAdapter.setEditMode(false);
                followAdapter.setEditMode(false);
            } else {
                btnEdit.setColorFilter(Color.parseColor("#3CBEF5"));
                if (mytab.getCurrentTab() == 0) {
                    llayout.setVisibility(View.VISIBLE);
                    historyAdapter.setEditMode(true);
                    followAdapter.setEditMode(false);
                } else if (mytab.getCurrentTab() == 1) {
                    llayoutFollow.setVisibility(View.VISIBLE);
                    followAdapter.setEditMode(true);
                    historyAdapter.setEditMode(false);
                }
            }
            isEditButtonColored = !isEditButtonColored;
        });

        btnChooseAll.setOnClickListener(v -> {
            if (isAllSelected) {
                historyAdapter.deselectAll();
                btnChooseAll.setText(R.string.select_all);
            } else {
                btnChooseAll.setText(R.string.deselect_all);
                historyAdapter.selectAll();
            }
            isAllSelected = !isAllSelected; // Toggle state
        });

        btnChooseAllFollow.setOnClickListener(v -> {
            if (isAllSelected) {
                followAdapter.deselectAll();
                btnChooseAllFollow.setText(R.string.select_all);
            } else {
                btnChooseAllFollow.setText(R.string.deselect_all);
                followAdapter.selectAll();
            }
            isAllSelected = !isAllSelected; // Toggle state
        });

        historyDB = new HistoryDB();
        user_id = "admin001";

        rcvHistory = findViewById(R.id.rcvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryAdapter(); // sử dụng adapter cho lịch sử
        historyAdapter.setData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        rcvHistory.setAdapter(historyAdapter);
        getHistory();

        rcvFollow = findViewById(R.id.rcvFollow);
        LinearLayoutManager linearLayoutManagerFollow = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvFollow.setLayoutManager(linearLayoutManagerFollow);

//        followAdapter = new FollowAdapter(getList()); // sử dụng adapter cho theo dõi
//        rcvFollow.setAdapter(followAdapter);

        mytab.setOnTabChangedListener(tabId -> {
            // Reset edit mode khi chuyển tab
            btnEdit.clearColorFilter();
            llayout.setVisibility(View.GONE);
            llayoutFollow.setVisibility(View.GONE);
            historyAdapter.setEditMode(false);
            followAdapter.setEditMode(false);
            isEditButtonColored = false;
        });
    }

//    private List<ListBookCase> getList() {
//        List<ListBookCase> list = new ArrayList<>();
//        list.add(new ListBookCase(R.drawable.tinhtukiemsi, "Tinh tú kiếm sĩ"));
//        list.add(new ListBookCase(R.drawable.ic_oce_piece, "ONE PIECE"));
//        list.add(new ListBookCase(R.drawable.tinhtukiemsi, "Tinh tú kiếm sĩ 2"));
//        return list;
//    }

    private void getHistory() {
        historyDB.getComicsReadByUser(user_id, (history, comic, chapter) -> {
            if (history != null && comic != null && chapter != null) {
                Log.d("TAG", "getComic: " + comic.get(0).getTitle());
                historyAdapter.setData(history, comic, chapter);
                historyAdapter.notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        editSearch.clearFocus();
        editSearchFollow.clearFocus();
        return false;
    }
}
