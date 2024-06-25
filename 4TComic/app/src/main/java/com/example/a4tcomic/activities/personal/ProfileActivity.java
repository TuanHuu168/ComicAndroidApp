package com.example.a4tcomic.activities.personal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView btn_save, tv_num;
    private EditText et_name;
    private CircleImageView iv_avatar;
    private ImageButton btn_return;
    private Button btn_change_avatar, btn_delete_avatar;

    private User currentUser;
    private String odlAvatarUrl;
    private Uri imageUri = null;
    private boolean isDeleteAvatar = false;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private UsersDB usersDB;

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
        tv_num = findViewById(R.id.tv_num);
        iv_avatar = findViewById(R.id.iv_avatar);
        btn_return = findViewById(R.id.btn_return);
        btn_save = findViewById(R.id.btn_save);
        btn_change_avatar = findViewById(R.id.btn_change_avatar);
        btn_delete_avatar = findViewById(R.id.btn_delete_avatar);

        // get data
        Bundle bundle = getIntent().getExtras();
        currentUser = (User) bundle.getSerializable("currentUser");
        odlAvatarUrl = currentUser.getAvatar_url();

        // set data
        et_name.setText(currentUser.getUsername());
        if (!currentUser.getAvatar_url().equals("")) {
            Picasso.get().load(currentUser.getAvatar_url()).into(iv_avatar);
        }
        usersDB = new UsersDB();

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                imageUri = result.getData().getData();
                iv_avatar.setImageURI(imageUri);
                currentUser.setAvatar_url(imageUri.toString());
            }
        });

        // set số lượng text nếu vượt quá 30 không cho nhập
        int rule_length = 30;
        tv_num.setText(currentUser.getUsername().length() + " / " + rule_length);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tv_num.setText(length + " / " + rule_length);

                if (length > rule_length) {
                    et_name.setText(s.subSequence(0, rule_length));
                    et_name.setSelection(rule_length); // Move cursor to end of text
                } else if (length == rule_length){
                    tv_num.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tv_num.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        // Trở lại trang trước
        btn_return.setOnClickListener(v -> finish());

        // xóa ảnh
        btn_delete_avatar.setOnClickListener(v -> {
            imageUri = null;
            isDeleteAvatar = true;
            iv_avatar.setImageResource(R.drawable.avatar);
        });

        // mở thư viện ảnh
        btn_change_avatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        // lưu thay đổi user
        btn_save.setOnClickListener(v -> {
           saveUser();
           finish();
        });

        // hide keyboard
        findViewById(R.id.main).setOnTouchListener(this);
    }

    private void saveUser() {
        currentUser.setUsername(et_name.getText().toString());
        if (!odlAvatarUrl.equals("") && isDeleteAvatar)
            currentUser.setAvatar_url("");
        usersDB.updateUser(currentUser, odlAvatarUrl, () ->
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        et_name.clearFocus();
        return false;
    }
}