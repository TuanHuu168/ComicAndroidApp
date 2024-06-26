package com.example.a4tcomic.activities.personal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.User;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnTouchListener {

    private EditText et_current_password, et_new_password, et_confirm_new_password;
    private Button btn_save;
    private ImageButton btn_back;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_current_password = findViewById(R.id.et_current_password);
        et_new_password = findViewById(R.id.et_new_password);
        et_confirm_new_password = findViewById(R.id.et_confirm_new_password);
        btn_save = findViewById(R.id.btn_save_change_pass);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(v -> finish());

        curUser = (User) getIntent().getSerializableExtra("curUser");

        btn_save.setOnClickListener(v -> {
            if (validateInput()) {
                saveChange();
            }
        });

        // hide keyboard
        findViewById(R.id.main).setOnTouchListener(this);

    }

    private void saveChange() {
        curUser.setPassword(et_new_password.getText().toString());

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", curUser.getPassword());

        UsersDB usersDB = new UsersDB();
        usersDB.updateUser(curUser, curUser.getAvatar_url(), () -> {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private boolean validateInput() {
        String curPass = et_current_password.getText().toString().trim();
        String newPass = et_new_password.getText().toString().trim();
        String confirmPass = et_confirm_new_password.getText().toString().trim();

        if (curPass.isEmpty()) {
            et_current_password.setError(getString(R.string.current_password_required));
            et_current_password.requestFocus();
            return false;
        }

        if (newPass.isEmpty()) {
            et_new_password.setError(getString(R.string.new_password_required));
            et_new_password.requestFocus();
            return false;
        }

        if (confirmPass.isEmpty()) {
            et_confirm_new_password.setError(getString(R.string.confirm_password_required));
            et_confirm_new_password.requestFocus();
            return false;
        }

        if (!curPass.equals(curUser.getPassword())) {
            et_current_password.setError(getString(R.string.incorrect_current_password));
            et_current_password.requestFocus();
            return false;
        }

        if (!newPass.equals(confirmPass)) {
            et_confirm_new_password.setError(getString(R.string.password_mismatch));
            et_confirm_new_password.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        et_current_password.clearFocus();
        et_new_password.clearFocus();
        et_confirm_new_password.clearFocus();
        return false;
    }
}
