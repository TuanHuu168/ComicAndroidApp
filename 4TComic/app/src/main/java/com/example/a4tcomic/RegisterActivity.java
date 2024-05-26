package com.example.a4tcomic;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    // Khai báo
    EditText edtUserName, edtEmail, edtPassword, edtRePassword;
    TextView lblLogin;
    RadioButton rd_terms;
    Button btnRegister;
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
        rd_terms = findViewById(R.id.rd_terms);
        btnRegister = findViewById(R.id.btnRegister);

        // Gạch chân login
        lblLogin.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // Đổi màu chữ và gạch chân điều khoản & dịch vụ
        String fullText = getString(R.string.terms_text);
        String underlinedText = getString(R.string.terms_text_underline);

        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf(underlinedText);
        int end = start + underlinedText.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6C91")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        rd_terms.setText(spannableString);

        // Sự kiện
        lblLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}