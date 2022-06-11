package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mny.Controller.AddMarket;
import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class AddMarketActivity1 extends AppCompatActivity {

    AddMarket addMarket_control;
    String email;
    String pwd;
    String check;
    String name;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmarket1);

        Button market_info = findViewById(R.id.market_info);
        Button sendmail = findViewById(R.id.sendmail);
        ImageView back = findViewById(R.id.back);

        market_info.setOnClickListener(onClickListener);
        sendmail.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        addMarket_control = new AddMarket(AddMarketActivity1.this);
        addMarket_control.setType("Market");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.market_info:
                    if(findViewById(R.id.sendmail).isClickable()) { addMarket_control.startToast("이메일 인증을 먼저 진행해주세요"); }
                    else {
                        pwd = ((EditText)findViewById(R.id.input_pwd)).getText().toString();
                        check = ((EditText)findViewById(R.id.input_check)).getText().toString();
                        name = ((EditText)findViewById(R.id.input_name)).getText().toString();
                        number = ((EditText)findViewById(R.id.input_number)).getText().toString();
                        Market user = new Market(email, number, 0, false, name);
                        addMarket_control.setMarket(user);
                        addMarket_control.isCheckedMail(pwd, check);
                    }
                    break;
                case R.id.sendmail:
                    email = ((EditText)findViewById(R.id.input_email)).getText().toString();
                    addMarket_control.sendMail(email, findViewById(R.id.sendmail));
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(AddMarketActivity1.this, "입력한 내용이 사라집니다.", "확인", "취소", LoginActivity.class, this::onBackPressed, null);
        tpd.show();
    }

}