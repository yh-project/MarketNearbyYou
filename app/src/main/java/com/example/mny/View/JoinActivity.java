package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mny.Controller.Join;
import com.example.mny.Model.Customer;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class JoinActivity extends AppCompatActivity {

    Join join_control;
    String email;
    String pwd;
    String check;
    String nickname;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Button sendmail = findViewById(R.id.sendmail);
        Button join = findViewById(R.id.join);
        ImageView back = findViewById(R.id.back);

        join.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        sendmail.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        join_control = new Join(JoinActivity.this);
        join_control.setType("Customer");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.sendmail:
                    email = ((EditText)findViewById(R.id.input_email)).getText().toString();
                    join_control.sendMail(email, findViewById(R.id.sendmail));
                    break;
                case R.id.join:
                    if(findViewById(R.id.sendmail).isClickable()) { join_control.startToast("이메일 인증을 먼저 진행해주세요"); }
                    else {
                        pwd = ((EditText) findViewById(R.id.input_pwd)).getText().toString();
                        check = ((EditText) findViewById(R.id.input_check)).getText().toString();
                        nickname = ((EditText) findViewById(R.id.input_nickname)).getText().toString();
                        number = ((EditText) findViewById(R.id.input_number)).getText().toString();
                        join_control.setCustomer(new Customer(email, number, 0, false, nickname));
                        join_control.signup(pwd, check);
                    }
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(JoinActivity.this, "입력한 내용이 사라집니다.", "확인", "취소", LoginActivity.class, this::onBackPressed, null);
        tpd.show();
    }
}