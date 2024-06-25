package com.example.a4tcomic.activities.personal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.CategoryAdapter;
import com.example.a4tcomic.db.AuthorsDB;
import com.example.a4tcomic.db.ComicsDB;
import com.example.a4tcomic.db.GenresDB;
import com.example.a4tcomic.models.Author;
import com.example.a4tcomic.models.Comic;
import com.example.a4tcomic.models.Genre;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class UploadComicActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_COVER_REQUEST = 1;
    private static final int PICK_IMAGE_AVATAR_REQUEST = 2;

    Button btnUpload;
    EditText edtComicName, edtAuthor, edtSummary;
    ImageView imgCover, imgAvatar;
    ImageButton btnBack;
    RadioButton rdTerms;
    Spinner spCategory;
    RecyclerView rvSelectedCategories;
    CategoryAdapter categoryAdapter;
    List<String> selectedCategories;
    Uri coverImageUri, avatarImageUri;
    GenresDB genresDB;
    AuthorsDB authorsDB;
    ComicsDB comicsDB;
    String authorId = "";
    boolean[] isChecked = {false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_comic);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        edtComicName = findViewById(R.id.edtComicName);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtSummary = findViewById(R.id.edtSummary);
        imgCover = findViewById(R.id.imgCover);
        imgAvatar = findViewById(R.id.imgAvatar);
        rdTerms = findViewById(R.id.rdUpdateTerms);
        spCategory = findViewById(R.id.spCategory);
        rvSelectedCategories = findViewById(R.id.rvSelectedCategories);
        btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(v -> { finish(); });
        comicsDB = new ComicsDB();

        selectedCategories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(selectedCategories);
        rvSelectedCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSelectedCategories.setAdapter(categoryAdapter);

        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu thể loại
        genresDB.getAllGenres(new GenresDB.GenreCallback() {
            @Override
            public void onGenres(List<Genre> genreList) {
                updateSpinnerWithGenres(genreList);
            }
        });

        // Đổi màu radioButton
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.radio_color);
        rdTerms.setButtonTintList(colorStateList);

        // Đổi màu chữ và gạch chân điều khoản & dịch vụ
        String fullText = getString(R.string.upload_terms_text);
        String underlinedText = getString(R.string.terms_text_underline);

        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf(underlinedText);
        int end = start + underlinedText.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6C91")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        rdTerms.setText(spannableString);

        // Sự kiện
        rdTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdTerms.setChecked(!isChecked[0]);
                isChecked[0] = !isChecked[0];
            }
        });

        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(PICK_IMAGE_COVER_REQUEST);
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(PICK_IMAGE_AVATAR_REQUEST);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadComic();
            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {  // Bỏ qua mục "Chọn thể loại"
                    String selectedCategory = parent.getItemAtPosition(position).toString();
                    if (!selectedCategories.contains(selectedCategory)) {
                        selectedCategories.add(selectedCategory);
                        categoryAdapter.updateCategories(selectedCategories);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không chọn gì
            }
        });
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_COVER_REQUEST) {
                coverImageUri = data.getData();
                imgCover.setImageURI(coverImageUri);
            } else if (requestCode == PICK_IMAGE_AVATAR_REQUEST) {
                avatarImageUri = data.getData();
                imgAvatar.setImageURI(avatarImageUri);
            }
        }
    }

    private void updateSpinnerWithGenres(List<Genre> genreList) {
        List<String> genreNames = new ArrayList<>();
        genreNames.add(getString(R.string.select_category));  // Lựa chọn ban đầu
        for (Genre genre : genreList) {
            genreNames.add(genre.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void uploadComic() {
        String comicName = edtComicName.getText().toString().trim();
        String authorName = edtAuthor.getText().toString().trim();
        String summary = edtSummary.getText().toString().trim();

        if (comicName.isEmpty() || authorName.isEmpty() || summary.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        authorsDB.getAllAuthors(new AuthorsDB.AllAuthorsCallback() {
            @Override
            public void onAllAuthorsLoaded(List<Author> authors) {
                boolean authorExists = false;
                for (Author author : authors) {
                    if (author.getName().equalsIgnoreCase(authorName)) {
                        authorExists = true;
                        authorId = author.getId();
                        // Sau khi kiểm tra và thêm tác giả, lưu thông tin truyện vào Firebase
                        uploadImagesAndSaveComic(comicName, authorId, summary);
                        break;
                    }
                }
                if (!authorExists) {
                    authorsDB.addAuthor(new Author("", authorName));
                }

            }
        });
    }

    private void uploadImagesAndSaveComic(String comicName, String authorId, String summary) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("comic_images");

        if (coverImageUri != null && avatarImageUri != null) {
            StorageReference coverRef = storageRef.child("covers/" + System.currentTimeMillis() + ".jpg");
            StorageReference avatarRef = storageRef.child("avatars/" + System.currentTimeMillis() + ".jpg");

            coverRef.putFile(coverImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    coverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri coverUri) {
                            avatarRef.putFile(avatarImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri avatarUri) {
                                            saveComicToFirebase(comicName, coverUri.toString(), avatarUri.toString(), authorId, summary);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Toast.makeText(this, getString(R.string.select_img), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveComicToFirebase(String comicName, String coverUrl, String avatarUrl, String authorId, String summary) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");

        Comic comic = new Comic("", comicName, coverUrl, avatarUrl, authorId, summary, userId, System.currentTimeMillis());
        comicsDB.addComic(comic);
        genresDB.getAllGenres(new GenresDB.GenreCallback() {
            @Override
            public void onGenres(List<Genre> genreList) {
                for (String selectedCategory : selectedCategories) {
                    for (Genre genre : genreList) {
                        if (genre.getTitle().equals(selectedCategory)) {
                            genresDB.addComicGenre(comic.getId(), genre.getId());
                            break;
                        }
                    }
                }
            }
        });
        Toast.makeText(this, "Comic uploaded successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
