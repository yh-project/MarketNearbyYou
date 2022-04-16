package com.example.mny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signup).setOnClickListener(onClickListener);
        findViewById(R.id.sendmail).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        String state = ((Button)findViewById(R.id.sendmail)).getText().toString();
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signup:
                    signup();
                    break;
                case R.id.sendmail:
                    if(state.equals(""))
                    send_email();
                    break;
            }
        }
    };

    private void signup() {

    }

    private void send_email() {
        String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
        String pass = ((EditText)findViewById(R.id.input_pwd)).getText().toString();
        String check = ((EditText)findViewById(R.id.input_check)).getText().toString();

        if(email.length() > 0 && pass.length() > 0 && check.length() > 0) {
            if(pass.length() >= 6) {
                if(pass.equals(check)) {
                    mAuth.createUserWithEmailAndPassword(email, "000000")
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = mAuth.getCurrentUser();
                                        if(user != null) {
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("send", "이메일 전송 완료");
                                                                startToast("인증 메일을 전송했습니다.");
                                                                ((Button)findViewById(R.id.sendmail)).setText("인증 확인");
                                                            }
                                                        }
                                                    });
                                        }
                                        Log.d("success", "회원가입 성공");
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            startToast("이메일이 이미 전송되엇습니다");
                                        }
                                        else if (task.getException() != null) {
                                            startToast(task.getException().toString());
                                        }
                                    }
                                }
                            });
                } else {
                    Log.d("unequal", "비밀번호를 똑같이 입력해주세요.");
                }
            } else {
                Log.d("short", "비밀번호는 6자 이상 입력해주세요.");
            }
        } else {
            Log.d("empty", "이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void checkVerify() {
        user = mAuth.getCurrentUser();
        if(user != null) {
            if(user.isEmailVerified()) {
                ((Button)findViewById(R.id.sendmail)).setText("인증 완료");
                ((Button)findViewById(R.id.sendmail)).setClickable(false);
            }
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}