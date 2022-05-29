package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class MEditProfileActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditprofile1);


    }

    @Override
    public void onBackPressed() {
        //TwoPickDialog tpd = new TwoPickDialog(MEditProfileActivity1.this, "앱을 종료시키겠습니까?", "종료", "취소");
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