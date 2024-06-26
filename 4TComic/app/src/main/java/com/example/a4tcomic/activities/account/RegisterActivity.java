package com.example.a4tcomic.activities.account;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import com.example.a4tcomic.R;
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    // Khai báo
    EditText edtUserName, edtEmail, edtPassword, edtRePassword;
    TextView lblLogin;
    RadioButton rdTerms;
    Button btnRegister;
    ImageView imgUser;
    boolean[] isChecked = {false};
    Uri imgUri;
    UsersDB usersDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        lblLogin = findViewById(R.id.lblLogin);
        rdTerms = findViewById(R.id.rdTerms);
        btnRegister = findViewById(R.id.btnRegister);
        imgUser = findViewById(R.id.user_image);
        usersDB = new UsersDB();

        // Gạch chân login
        lblLogin.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // Đổi màu button
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.radio_color);
        rdTerms.setButtonTintList(colorStateList);

        // Đổi màu chữ và gạch chân điều khoản & dịch vụ
        String fullText = getString(R.string.terms_text);
        String underlinedText = getString(R.string.terms_text_underline);

        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf(underlinedText);
        int end = start + underlinedText.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6C91")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        rdTerms.setText(spannableString);

        // Sự kiện
        lblLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rdTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đổi trạng thái check của radio button
                rdTerms.setChecked(!isChecked[0]);
                isChecked[0] = !isChecked[0];
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String rePassword = edtRePassword.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rePassword)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isChecked[0]) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.agree_terms), Toast.LENGTH_SHORT).show();
                    return;
                }

                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setCreated_at(System.currentTimeMillis()); // Thiết lập thời gian tạo acc

                checkUserExistsAndRegister(username, email, newUser);
            }
        });
    }

//    private long convertTime() {
//        long time = 0;
//        String dateTime = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString();
//        // chuyển từ string sang long
//        if (!TextUtils.isEmpty(dateTime)) {
//            time = Long.parseLong(dateTime.replace("-", "")
//                    .replace(" ", "")
//                    .replace(":", ""));
//        }
//        return time;
//    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailPattern);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            imgUser.setImageURI(imgUri);
        }
    }

    private void checkUserExistsAndRegister(String username, String email, User newUser) {
        usersDB.getAllUsers(new UsersDB.AllUsersCallback() {
            @Override
            public void onAllUsersLoaded(List<User> users) {
                boolean userExists = false;
                for (User user : users) {
                    if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                        userExists = true;
                        break;
                    }
                }
                if (userExists) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.account_exists), Toast.LENGTH_SHORT).show();
                } else {
                    if (imgUri != null) {
                        uploadImageToFirebase(newUser);
                    } else {
                        saveUserToDatabase(newUser);
                    }
                }
            }
        });
    }


    private void uploadImageToFirebase(User user) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("comic_db/avatar_users/" + UUID.randomUUID().toString());
        storageRef.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        user.setAvatar_url(uri.toString());
                        saveUserToDatabase(user);
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.registration_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserToDatabase(User user) {
        usersDB.addUser(user);
        Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
        finish();
    }
}