package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class AddMarketActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmarket1);

        Button market_info = findViewById(R.id.market_info);
        Button sendmail = findViewById(R.id.sendmail);

        market_info.setOnClickListener(onClickListener);
        sendmail.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.market_info:
                    startActivity(AddMarketActivity2.class);
                    break;
                case R.id.sendmail:
                    startToast("이미 존재하는 가게 이름(지점명) 입니다.");
            }
        }
    };

    @Override
    public void onBackPressed() {
        //TwoPickDialog tpd = new TwoPickDialog(AddMarketActivity1.this, "입력한 내용이 사라집니다.", "확인", "취소");
        //tpd.show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}