package com.example.a4tcomic.activities.account;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.a4tcomic.R;
import com.example.a4tcomic.activities.HomePageActivity;
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    // Khai báo
    EditText edtUserName, edtPassword;
    TextView lblForgot, lblRegister;
    Button btnLogin;
    UsersDB usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgotMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        edtUserName = findViewById(R.id.edtUserName);
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
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        int status = sharedPreferences.getInt("status", 0);

        if (!userId.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            autoLogin(userId, email, password, status);
        }

        // Chuyển trang quên mật khẩu và đăng ký
        lblForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotIntent);
            }
        });

        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                } else {
                    usersDB.getAllUsers(new UsersDB.AllUsersCallback() {
                        @Override
                        public void onAllUsersLoaded(List<User> users) {
                            boolean userFound = false;
                            for (User user : users) {
                                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                                    if (user.getStatus() == 1) {
                                        Toast.makeText(LoginActivity.this, getString(R.string.account_locked), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    userFound = true;
                                    saveLoginState(user.getId(), username, user.getEmail(), password, user.getStatus());
                                    Intent homePageIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                                    startActivity(homePageIntent);
                                    finish();
                                    break;
                                }
                            }
                            if (!userFound) {
                                Toast.makeText(LoginActivity.this, getString(R.string.incorrect_credentials), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void saveLoginState(String id, String username, String email, String password, int status) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putInt("status", status);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void autoLogin(String userId, String email, String password, int status) {
        boolean check = false;
        usersDB.getUserById(userId, new UsersDB.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                boolean check = false;
                if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    if (status == 1) {
                        check = false;
                    }
                }
                if (check) {
                    Intent homePageIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(homePageIntent);
                    finish();
                } else {
                    // Nếu tài khoản không hợp lệ, xóa trạng thái đăng nhập
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                }
            }
        });
    }
}
