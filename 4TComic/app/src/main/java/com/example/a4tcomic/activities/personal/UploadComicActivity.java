package com.example.a4tcomic.activities.personal;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;

public class UploadComicActivity extends AppCompatActivity {
    Button btnUpload;
    EditText edtComicName, edtOriginalName, edtAuthor, edtSummary;
    ImageView imgCover, imgContent;
    ImageButton btnBack;
    RadioButton rdTerms;
    Spinner spCategory;
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
        rdTerms = findViewById(R.id.rdUpdateTerms);
        spCategory = findViewById(R.id.spCategory);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> { finish(); });

        String[] items = {"Chọn thể loại", "Tu tiên", "Xuyên không", "Hành động", "Tình cảm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

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
                if (isChecked[0]) {
                    rdTerms.setChecked(false);
                    isChecked[0] = false;
                } else {
                    rdTerms.setChecked(true);
                    isChecked[0] = true;
                }
            }
        });
    }
}