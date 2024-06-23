package com.example.a4tcomic.activities.account;

import android.graphics.Paint;
import android.os.Bundle;
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
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.User;

import java.util.List;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Khai báo
    EditText edtUserName, edtEmail;
    TextView lblLogin;
    Button btnForgot;
    UsersDB usersDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgotMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        lblLogin = findViewById(R.id.lblLogin);
        btnForgot = findViewById(R.id.btnForgot);
        usersDB = new UsersDB();

        // Gạch chân
        lblLogin.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // Sự kiện
        lblLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                } else {
                    usersDB.getAllUsers(new UsersDB.AllUsersCallback() {
                        @Override
                        public void onAllUsersLoaded(List<User> users) {
                            boolean userFound = false;
                            for (User user : users) {
                                if (user.getUsername().equals(username) && user.getEmail().equals(email)) {
                                    userFound = true;
                                    String newPassword = generateRandomPassword();
                                    user.setPassword(newPassword);
                                    usersDB.updateUser(user, user.getAvatar_url(), () -> {
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.random_password_created), Toast.LENGTH_SHORT).show();
                                    });
                                    break;
                                }
                            }
                            if (!userFound) {
                                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.account_not_exists), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private String generateRandomPassword() {
        int passwordLength = 8;
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            password.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return password.toString();
    }
}