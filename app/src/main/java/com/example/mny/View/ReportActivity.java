package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.mny.Controller.Report;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class ReportActivity extends AppCompatActivity {

    private EditText input_names;
    private EditText input_contents;
    private Button report;
    private String type;
    private Report reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        input_names = findViewById(R.id.input_names);
        input_contents = findViewById(R.id.input_contents);
        report = findViewById(R.id.report);

        report.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.report:
                    reports = new Report(ReportActivity.this, input_names.getText().toString(), input_contents.getText().toString(), type);
                    reports.report();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(ReportActivity.this, "메인화면으로 돌아가시겠습니까?", "확인", "취소", CProfileActivity.class, null, null);
        tpd.show();
    }
}