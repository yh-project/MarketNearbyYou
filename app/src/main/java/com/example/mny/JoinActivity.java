package com.example.mny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {

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

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.sendmail:
                    startToast("이미 존재하는 이메일 입니다.");
                    break;
                case R.id.join:
                    join();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    public void join() {
        String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
        String nickname = ((EditText)findViewById(R.id.input_nickname)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.input_pwd)).getText().toString();
        String pwd_check = ((EditText)findViewById(R.id.input_check)).getText().toString();
        String number = ((EditText)findViewById(R.id.input_number)).getText().toString();

        if(email.length()==0 || nickname.length()==0 || pwd.length()==0 || pwd_check.length()==0 || number.length()==0) startToast("비어있는 입력이 있습니다.");
        else if(pwd.length()<6) startToast("비밀번호는 6자 이상 입력해주세요.");
        else if(!pwd.equals(pwd_check)) startToast("비밀번호가 다르게 입력되었습니다.");
    }

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(JoinActivity.this, "입력한 내용이 사라집니다.", "확인", "취소");
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