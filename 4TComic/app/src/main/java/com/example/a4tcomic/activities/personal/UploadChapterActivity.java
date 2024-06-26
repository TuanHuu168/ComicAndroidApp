package com.example.a4tcomic.activities.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;
import com.example.a4tcomic.db.ChaptersDB;
import com.example.a4tcomic.models.Chapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadChapterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_PDF_REQUEST = 2;

    EditText edtComicName, edtTitle, edtChapter;
    ImageButton btnBack;
    Button btnUpload;
    ImageView imgCover, imgPDF;

    Uri coverImageUri, pdfUri;
    String comicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_chapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        edtComicName = findViewById(R.id.edtComicName);
        edtTitle = findViewById(R.id.edtTitle);
        edtChapter = findViewById(R.id.edtChapter);
        btnBack = findViewById(R.id.btnBack);
        btnUpload = findViewById(R.id.btnUpload);
        imgCover = findViewById(R.id.imgCover);
        imgPDF = findViewById(R.id.imgPDF);

        // Nhận Intent và hiển thị tên truyện
        String comicName = getIntent().getStringExtra("comicName");
        comicId = getIntent().getStringExtra("comicId");
        edtComicName.setText(comicName);
        setLastestChapterNumber(comicId);

        btnBack.setOnClickListener(v -> finish());

        imgCover.setOnClickListener(v -> openFileChooser(PICK_IMAGE_REQUEST));

        imgPDF.setOnClickListener(v -> openFileChooser(PICK_PDF_REQUEST));

        btnUpload.setOnClickListener(v -> uploadChapter());
    }

    private void setLastestChapterNumber(String comicId){
        ChaptersDB chaptersDB = new ChaptersDB();
        chaptersDB.getLastestChapter(comicId, new ChaptersDB.ChapterCallback() {
            @Override
            public void onChapterLoaded(Chapter lastestChapter) {
                if (lastestChapter == null) {
                    edtChapter.setText("1");
                } else {
                    edtChapter.setText(String.valueOf(lastestChapter.getOrder() + 1));
                }
            }
        });
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType(requestCode == PICK_IMAGE_REQUEST ? "image/*" : "application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                coverImageUri = data.getData();
                imgCover.setImageURI(coverImageUri);
            } else if (requestCode == PICK_PDF_REQUEST) {
                pdfUri = data.getData();
                imgPDF.setImageResource(R.drawable.ic_pdf); // Change this to an appropriate PDF icon
            }
        }
    }

    private void uploadChapter() {

        if (coverImageUri == null || pdfUri == null || edtTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("chapters/"+comicId);
        StorageReference coverRef = storageRef.child("covers/" + System.currentTimeMillis() + ".jpg");
        StorageReference pdfRef = storageRef.child("pdfs/" + System.currentTimeMillis() + ".pdf");

        coverRef.putFile(coverImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                coverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri coverUri) {
                        pdfRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri pdfUri) {
                                        saveChapterToFirebase(edtTitle.getText().toString(), coverUri.toString(), pdfUri.toString());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadChapterActivity.this, getString(R.string.pdf_upload_fail), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadChapterActivity.this, getString(R.string.cover_image_upload_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChapterToFirebase(String title, String coverUrl, String pdfUrl) {
        ChaptersDB chaptersDB = new ChaptersDB();
        Chapter chapter = new Chapter("", title, coverUrl, pdfUrl, Integer.parseInt(edtChapter.getText().toString()), comicId, System.currentTimeMillis());
        chaptersDB.addChapter(chapter);
        Toast.makeText(this, getString(R.string.chapter_upload_success), Toast.LENGTH_SHORT).show();
        finish();
    }
}
