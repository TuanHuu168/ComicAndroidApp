package com.example.a4tcomic.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.TabHost;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat.Type;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.FollowAdapter;
import com.example.a4tcomic.adapters.HistoryAdapter;
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
        spec2 = mytab.newTabSpec("theodoi");   // tạo mới tab
        spec2.setContent(R.id.tabFollow); // tham chiếu id tab con
        spec2.setIndicator("Theo dõi");
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
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa lịch sử truyện này không?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Xóa đơn vị khỏi SQLite
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        btnDeleteFollow.setOnClickListener(v -> {
            new AlertDialog.Builder(BookcaseActivity.this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa truyện theo dõi này không?")
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
                btnChooseAll.setText("Chọn tất cả");
            } else {
                btnChooseAll.setText("Bỏ chọn tất cả");
                historyAdapter.selectAll();
            }
            isAllSelected = !isAllSelected; // đổi trạng thái
        });

        btnChooseAllFollow.setOnClickListener(v -> {
            if (isAllSelected) {
                followAdapter.deselectAll();
                btnChooseAllFollow.setText("Chọn tất cả");
            } else {
                btnChooseAllFollow.setText("Bỏ chọn tất cả");
                followAdapter.selectAll();
            }
            isAllSelected = !isAllSelected; // đổi trạng thái
        });

        rcvHistory = findViewById(R.id.rcvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryAdapter(getList()); // sử dụng adapter cho lịch sử
        rcvHistory.setAdapter(historyAdapter);

        rcvFollow = findViewById(R.id.rcvFollow);
        LinearLayoutManager linearLayoutManagerFollow = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvFollow.setLayoutManager(linearLayoutManagerFollow);
        followAdapter = new FollowAdapter(getList()); // sử dụng adapter cho theo dõi
        rcvFollow.setAdapter(followAdapter);

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
