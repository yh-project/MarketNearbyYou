package com.example.mny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.login);
        Button join = findViewById(R.id.join);
        Button addmarket = findViewById(R.id.addMarket);
        ImageView back = findViewById(R.id.back);

        login.setOnClickListener(onClickListener);
        join.setOnClickListener(onClickListener);
        addmarket.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.join:
                    startActivity(JoinActivity.class);
                    break;
                case R.id.addMarket:
                    startActivity(AddMarketActivity1.class);
                    break;
                case R.id.login:
                    login();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;

            }
        }
    };

    public void login() {
        String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.input_pwd)).getText().toString();


        if(email.length()==0 || pwd.length()==0) startToast("이메일 또는 비밀번호를 입력해주세요.");
        else if(pwd.length()<6) startToast("비밀번호는 6자리 이상입니다.");
        else startToast("등록된 회원이 아닙니다.");
    }


    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(LoginActivity.this, "앱을 종료시키겠습니까?", "종료", "취소");
        tpd.show();
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