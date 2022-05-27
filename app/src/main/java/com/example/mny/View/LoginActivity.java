package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mny.Controller.Login;
import com.example.mny.Model.User;
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
            switch(v.getId()) {
                case R.id.join:
                    break;
                case R.id.addMarket:
                    break;
                case R.id.login:
                    String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
                    String password = ((EditText)findViewById(R.id.input_pwd)).getText().toString();
                    login_control = new Login(email, password, LoginActivity.this);
                    login_control.login();
                    break;
                case R.id.back:
                    //onBackPressed();
                    break;

            }
        }
    };

    @Override
    public void onBackPressed() {
        if(login_control != null) {
            Log.d("sibal", "1");
            login_control.onBackPressed();
        } else {
            Log.d("sibal", "2");
            TwoPickDialog tpd = new TwoPickDialog(LoginActivity.this, "앱을 종료시키겠습니까?", "종료", "취소");
            tpd.show();
        }
    }
}