package com.example.a4tcomic.activities.story;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReadPageActivity extends AppCompatActivity {

    private ImageButton btnSelectChapter, btnPrevious, btnNext, btnBack, btnMusic;
    private TextView tvPage;
    private PDFView pdfView;
    private ProgressBar progressBar;

    private List<String> chapters;
    private List<Chapter> chapterList;
    private Chapter currentChapter;
    Boolean isMusicPlaying = false;

    private static final String TAG = "ReadPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_page);

        tvPage = findViewById(R.id.tvPageNumber);
        pdfView = findViewById(R.id.pdfView);
        btnSelectChapter = findViewById(R.id.btnChooseChapters);
        btnPrevious = findViewById(R.id.btnLeftArrow);
        btnNext = findViewById(R.id.btnRightArrow);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());

        // nhận dữ liệu
        chapterList = (List<Chapter>) getIntent().getSerializableExtra("chapterList");
        Log.d(TAG, "list chapter size: " + chapterList.size());
        currentChapter = (Chapter) getIntent().getSerializableExtra("chapter");
        Log.d(TAG, "current chapter: " + currentChapter.getTitle());

        // Khởi tạo danh sách các chương
        chapters = new ArrayList<>();
        for (Chapter chapter : chapterList) {
            int i = chapter.getOrder();
            chapters.add("Chương " + i);
        }

        loadPDF();

        btnSelectChapter.setOnClickListener(v -> showDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadPDF() {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(currentChapter.getPdf_url());
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            pdfView.fromBytes(bytes)
                    .swipeHorizontal(false) // cuộn theo chiều dọc
                    .enableAntialiasing(true) // cải thiện chất lượng hiển thị
                    .onPageChange( (page, pageCount) -> {
                        int i = page + 1;
                        tvPage.setText(i + "/" + pageCount);
                        Log.d(TAG, "loadPDF: " + i + "/" + pageCount);
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

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_chapters);
        RecyclerView recyclerView = dialog.findViewById(R.id.rcv_chapters);
        Button btnFilterNewest = dialog.findViewById(R.id.btn_filter_newest);
        Button btnFilterOldest = dialog.findViewById(R.id.btn_filter_oldest);

        // Thiết lập adapter và layout manager cho RecyclerView
        ChaptersAdapter adapter = new ChaptersAdapter(chapters);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Hiển thị 3 cột

        View.OnClickListener filterClickListener = new View.OnClickListener() {     // cung cấp để lắng nghe sự kiện nhấn trên 1 view
            @Override
            public void onClick(View v) {
                List<String> sortedChapters;    // danh sách sẽ chứa các chung đã được sắp xếp sau xử lý
                if (v.getId() == R.id.btn_filter_newest) {
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
                    sortedChapters = chapters.stream()
                            .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.replaceAll("[^0-9]", ""))))
                            .collect(Collectors.toList());
                    btnFilterOldest.setTextColor(Color.parseColor("#3CBEF5"));
                    btnFilterOldest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#3CBEF5")));
                    btnFilterNewest.setTextColor(Color.WHITE);
                    btnFilterNewest.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                }
                adapter.updateChapters(sortedChapters);
            }
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getResources().getDisplayMetrics().heightPixels * 0.67));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
