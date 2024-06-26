package com.example.a4tcomic.activities.story;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.ChaptersAdapter;
import com.example.a4tcomic.adapters.ReadingPagerAdapter;
import com.example.a4tcomic.models.Chapter;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReadPageActivity extends AppCompatActivity {

    private ImageButton btnSelectChapter, btnPrevious, btnNext, btnBack, btnMusic;
    private TextView tvPage, tvNameChapter;
    private PDFView pdfView;
    private ProgressBar progressBar;

    private List<String> chapters;
    private List<Chapter> chapterList, sortedListNew;
    private Chapter currentChapter, oldChapter;
    Boolean isMusicPlaying = false;
    private Boolean newest = true;

    private static final String TAG = "TAG_ReadPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_page);

        tvPage = findViewById(R.id.tvPageNumber);
        tvNameChapter = findViewById(R.id.tv_name_chapter);
        pdfView = findViewById(R.id.pdfView);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnMusic = findViewById(R.id.btnMusic);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMusic();
            }
        });

        btnSelectChapter = findViewById(R.id.btnChooseChapters);
        btnPrevious = findViewById(R.id.btnLeftArrow);
        btnNext = findViewById(R.id.btnRightArrow);
        progressBar = findViewById(R.id.progressBar);

        // nhận dữ liệu
        sortedListNew = (List<Chapter>) getIntent().getSerializableExtra("chapterList");
        chapterList = sortedListNew;
        chapterList.sort(Comparator.comparingInt(Chapter::getOrder));

        currentChapter = (Chapter) getIntent().getSerializableExtra("chapter");
        oldChapter = null;
        Log.d(TAG, "current chapter: " + currentChapter.getTitle());

        // Khởi tạo danh sách các chương
        chapters = new ArrayList<>();
        for (Chapter ch : chapterList) {
            int i = ch.getOrder();
            chapters.add("Chương " + i);
        }

        setLayout();

        btnPrevious.setOnClickListener(v -> {
            Log.d(TAG, "currentOrder: " + currentChapter.getOrder());
            Log.d(TAG, "fistChap: " + chapterList.get(0).getOrder());
            Log.d(TAG, "------------------------------------------");
            currentChapter = chapterList.get(currentChapter.getOrder()-2);
            setLayout();
        });

        btnNext.setOnClickListener(v -> {
            Log.d(TAG, "currentOrder: " + currentChapter.getOrder());
            Log.d(TAG, "fistChap: " + chapterList.get(0).getOrder());
            Log.d(TAG, "------------------------------------------");
            currentChapter = chapterList.get(currentChapter.getOrder());
            setLayout();
        });

        btnSelectChapter.setOnClickListener(v -> showDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setLayout() {
        // nếu vẫn là chapter đó thì không đổi gì cả
        if (currentChapter == oldChapter && oldChapter != null) return;

        oldChapter = currentChapter;
        newest = true;

        // set tv top
        tvNameChapter.setText(getString(R.string.chapter) + " " +
                        currentChapter.getOrder()+ ": "+
                        currentChapter.getTitle());
        tvPage.setText("1/1");

        // set layout nút bottom
        if (currentChapter.getOrder() == 1)
            btnPrevious.setVisibility(View.GONE);
        else
            btnPrevious.setVisibility(View.VISIBLE);
        if (currentChapter.getOrder() == chapterList.size())
            btnNext.setVisibility(View.GONE);
        else
            btnNext.setVisibility(View.VISIBLE);

        // nếu pdf rỗng thì return
        if (currentChapter.getPdf_url().equals("")|| currentChapter.getPdf_url() == null) return;
        loadPDF();
    }

    private void loadPDF() {
        // làm trống pdfView
        progressBar.setVisibility(View.VISIBLE);
        pdfView.fromFile(null).load();
        // tải pdf mới
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(currentChapter.getPdf_url());
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            if (bytes == null) {
                Toast.makeText(this, "Lỗi tải ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            pdfView.fromBytes(bytes)
                    .swipeHorizontal(false) // cuộn theo chiều dọc
                    .enableAntialiasing(true) // cải thiện chất lượng hiển thị
                    .onPageChange( (page, pageCount) -> {
                        int i = page + 1;
                        tvPage.setText(i + "/" + pageCount);
                    })
                    .onError(t -> {
                        Toast.makeText(this, "Lỗi khi tải PDF", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "loadPDF: ", t);
                    })
                    .onPageError((page, t) -> {
                        Toast.makeText(this, "Lỗi khi tải trang" + page + " " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "loadPDF: ", t);
                    })
                    .load();
            progressBar.setVisibility(View.GONE);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi khi tải PDF", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "loadPDF: ", e);
            progressBar.setVisibility(View.GONE);
        });
    }
    
    private void toggleMusic() {
        Intent intent = new Intent(ReadPageActivity.this, Music.class);
        if (isMusicPlaying) {
            stopService(intent);
            btnMusic.setImageResource(R.drawable.ic_adjust); // Cập nhật icon cho nút
        } else {
            startService(intent);
            btnMusic.setImageResource(R.drawable.ic_adjust); // Cập nhật icon cho nút
        }
        isMusicPlaying = !isMusicPlaying;
    }

    private void showDialog() {
        newest = true;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_chapters);
        RecyclerView recyclerView = dialog.findViewById(R.id.rcv_chapters);
        Button btnFilterNewest = dialog.findViewById(R.id.btn_filter_newest);
        Button btnFilterOldest = dialog.findViewById(R.id.btn_filter_oldest);

        // Thiết lập adapter và layout manager cho RecyclerView
        ChaptersAdapter adapter = new ChaptersAdapter(chapters, position -> {
            if (newest) {
                sortedListNew = chapterList;
                sortedListNew.sort((o1, o2) -> Integer.compare(o2.getOrder(), o1.getOrder()));
                currentChapter = sortedListNew.get(position);
                Log.d(TAG, "new: " + position + " is "+ newest);
                Log.d(TAG, "new0: " + sortedListNew.get(0).getOrder());
            }
            else {
                sortedListNew = chapterList;
                currentChapter = sortedListNew.get(position);
                Log.d(TAG, "cur: " + position + " is "+ newest);
            }
            setLayout();
            dialog.dismiss();
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Hiển thị 3 cột

        // cung cấp để lắng nghe sự kiện nhấn trên 1 view
        View.OnClickListener filterClickListener = v -> {
            List<String> sortedChapters;    // danh sách sẽ chứa các chung đã được sắp xếp sau xử lý
            if (v.getId() == R.id.btn_filter_newest) {
                newest = true;
                sortedChapters = chapters.stream()  // tạo 1 stream từ danh sách cac chương
                        .sorted((o1, o2) -> {   // sắp xếp các phần tử trong stream
                            int num1 = Integer.parseInt(o1.replaceAll("[^0-9]", ""));   // loại bỏ cac ký tự không phải là số
                            int num2 = Integer.parseInt(o2.replaceAll("[^0-9]", ""));
                            return Integer.compare(num2, num1); // so sánh
                        })
                        .collect(Collectors.toList());   // thu thập các phần tử đã sap vào 1 danh sách
                // cap nhật giao diện lọc mới
                btnFilterNewest.setTextColor(Color.parseColor("#3CBEF5"));
                btnFilterNewest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#3CBEF5")));
                btnFilterOldest.setTextColor(Color.WHITE);
                btnFilterOldest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
            } else {
                newest = false;
                sortedChapters = chapters.stream()
                        .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.replaceAll("[^0-9]", ""))))
                        .collect(Collectors.toList());
                btnFilterOldest.setTextColor(Color.parseColor("#3CBEF5"));
                btnFilterOldest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#3CBEF5")));
                btnFilterNewest.setTextColor(Color.WHITE);
                btnFilterNewest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
            }
            adapter.updateChapters(sortedChapters);
        };

        btnFilterNewest.setOnClickListener(filterClickListener);
        btnFilterOldest.setOnClickListener(filterClickListener);

        // Hiển thị dialog
        dialog.show();
        // Gọi sự kiện click của btnFilterNewest để mặc định chọn lọc mới nhất
        dialog.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                btnFilterNewest.performClick();
            }
        });
        // Đặt chiều cao cụ thể cho dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (getResources().getDisplayMetrics().heightPixels * 0.67));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
