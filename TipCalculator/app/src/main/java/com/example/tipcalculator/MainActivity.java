package com.example.tipcalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener,
        View.OnClickListener, TextWatcher {

    // define variables for the widgets
    private EditText bill_amount_et;
    private TextView percent_tv, tip_tv, toltal_tv;
    private Button percent_Down_btn, percent_Up_btn;
    private SharedPreferences savedValue; // SharedPreferences object

    // define instance that should be saved
    private String bill_amount_String = "";
    private float tip_Percent  = .05f;

    NumberFormat currency = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide(); // hide header_default android_studio

        // get references to the widgets
        bill_amount_et = (EditText) findViewById(R.id.bill_amount_et);
        percent_tv = findViewById(R.id.percent_tv);
        percent_Down_btn = findViewById(R.id.percent_Down_btn);
        percent_Up_btn = findViewById(R.id.percent_Up_btn);
        tip_tv = findViewById(R.id.tip_tv);
        toltal_tv = findViewById(R.id.total_tv);

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tip_tv.setText(currency.format(0));
        toltal_tv.setText(currency.format(0));

        NumberFormat percent = NumberFormat.getPercentInstance();
        percent_tv.setText(percent.format(tip_Percent));

        // set the listeners
        bill_amount_et.addTextChangedListener(this);
        bill_amount_et.setOnEditorActionListener(this);
        percent_Down_btn.setOnClickListener(this);
        percent_Up_btn.setOnClickListener(this);

        savedValue = getSharedPreferences("SavedValue", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValue.edit();
        editor.putString("billAmountString", bill_amount_String);
        editor.putFloat("tipPercent", tip_Percent);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get instance variables
        bill_amount_String = savedValue.getString("billAmountString", "");
        tip_Percent = savedValue.getFloat("tipPercent", 0.05f);

        // set bill amount on its widget
        bill_amount_et.setText(bill_amount_String);

        // calculate and display
        calculateAndDisplay();
    }

    public void calculateAndDisplay() {
        // get bill amount
        bill_amount_String = bill_amount_et.getText().toString();
        float billAmount;
        if (bill_amount_String.isEmpty()) {
            billAmount = 0;
        }
        else {
            billAmount = Float.parseFloat(bill_amount_String);
        }

        // calculate tip and total
        float tipAmount = billAmount * tip_Percent;
        float totalAmount = billAmount + tipAmount;

        // display result with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tip_tv.setText(currency.format(tipAmount));
        toltal_tv.setText(currency.format(totalAmount));

        NumberFormat percent = NumberFormat.getPercentInstance();
        percent_tv.setText(percent.format(tip_Percent));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.percent_Down_btn && tip_Percent > .05f) {
            tip_Percent -= .01f;
            calculateAndDisplay();
        } else if (v.getId() == R.id.percent_Up_btn) {
            tip_Percent += .01f;
            calculateAndDisplay();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {

    }
}