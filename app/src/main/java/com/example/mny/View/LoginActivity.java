package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mny.Controller.Login;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class LoginActivity extends AppCompatActivity {

    Login login_control;

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
            String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
            String password = ((EditText)findViewById(R.id.input_pwd)).getText().toString();
            login_control = new Login(email, password, LoginActivity.this);
            switch(v.getId()) {
                case R.id.join:
                    login_control.changePage("Join");
                    break;
                case R.id.addMarket:
                    login_control.changePage("AddMarket");
                    break;
                case R.id.login:
                    login_control.login();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;

            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(LoginActivity.this, "앱을 종료시키겠습니까?", "종료", "취소", null, this::onBackPressed, null);
        tpd.show();
    }
}