package com.example.a4tcomic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class activity_history_page extends AppCompatActivity {
    ImageButton btnBack, btnEdit;
    Button btnFollow, btnChooseAll;
    RecyclerView rcvHistory;
    BookAdapter bookAdapter;
    LinearLayout llayout;
    boolean isEditButtonColored = false; // Trạng thái ban đầu của nút
    boolean isAllSelected = false; // Trạng thái ban đầu của nút

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_page);
        btnBack = findViewById(R.id.btnBack);
        btnFollow = findViewById(R.id.btnFollow);
        btnEdit = findViewById(R.id.btnEdit);
        llayout = findViewById(R.id.llayout);
        llayout.setVisibility(View.GONE);
        btnChooseAll = findViewById(R.id.btnChooseAll);

        btnBack.setOnClickListener(v -> finish());
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
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFollow = new Intent(activity_history_page.this, activity_follow_page.class);
                startActivity(intentFollow);
            }
        });

        rcvHistory = findViewById(R.id.rcvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        bookAdapter = new BookAdapter(getList());
        rcvHistory.setAdapter(bookAdapter);
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
}
