package com.example.a4tcomic.activities.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.a4tcomic.R;
import com.example.a4tcomic.activities.HomePageActivity;
import com.example.a4tcomic.activities.personal.GraphicSettingActivity;
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.User;

import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    // Khai báo
    EditText edtPassword, edtEmail;
    TextView lblForgot, lblRegister;
    Button btnLogin;
    UsersDB usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Áp dụng cài đặt dark mode và ngôn ngữ
        applySettings();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgotMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        lblForgot = findViewById(R.id.lblForgot);
        lblRegister = findViewById(R.id.lblRegister);
        btnLogin = findViewById(R.id.btnLogin);
        usersDB = new UsersDB();

        // Gạch chân
        lblForgot.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        lblRegister.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        int status = sharedPreferences.getInt("status", 0);
        if (!userId.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            autoLogin(userId, email, password, status);
        }

        // Chuyển trang quên mật khẩu và đăng ký
        lblForgot.setOnClickListener(v -> {
            Intent forgotIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgotIntent);
        });

        lblRegister.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        btnLogin.setOnClickListener(v -> {
            String emailInput = edtEmail.getText().toString().trim();
            String passwordInput = edtPassword.getText().toString().trim();

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            } else {
                usersDB.getAllUsers(users -> {
                    boolean userFound = false;
                    for (User user : users) {
                        if (user.getEmail().equals(emailInput) && user.getPassword().equals(passwordInput)) {
                            if (user.getStatus() == 1) {
                                Toast.makeText(LoginActivity.this, getString(R.string.account_locked), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            userFound = true;
                            saveLoginState(user.getId(), user.getEmail(), passwordInput, user.getStatus());
                            Intent homePageIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                            startActivity(homePageIntent);
                            finish();
                            break;
                        }
                    }
                    if (!userFound) {
                        Toast.makeText(LoginActivity.this, getString(R.string.incorrect_credentials), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveLoginState(String id, String email, String password, int status) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putInt("status", status);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void autoLogin(String userId, String email, String password, int status) {
        usersDB.getUserById(userId, user -> {
            if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password)) {
                if (status == 0) {
                    Intent homePageIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(homePageIntent);
                    finish();
                } else {
                    clearLoginState();
                }
            } else {
                clearLoginState();
            }
        });
    }
}