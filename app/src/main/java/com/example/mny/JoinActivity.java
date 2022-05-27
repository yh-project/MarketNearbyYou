package com.example.mny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

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

        mAuth = FirebaseAuth.getInstance();

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.sendmail:
                    send_mail();
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

    private void send_mail() {
        String email = ((EditText)findViewById(R.id.input_email)).getText().toString();

        if(email.length() > 0) {
            mAuth.createUserWithEmailAndPassword(email, "000000")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                if(user != null) {
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        Log.d("send", "인증메일 전송 완료");
                                                        startToast("인증 메일을 전송했습니다.");
                                                    }
                                                }
                                            });
                                }
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
            Log.d("empty", "이메일을 입력해주세요.");
        }
    }

    public void join() {
        String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
        String nickname = ((EditText)findViewById(R.id.input_nickname)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.input_pwd)).getText().toString();
        String pwd_check = ((EditText)findViewById(R.id.input_check)).getText().toString();
        String number = ((EditText)findViewById(R.id.input_number)).getText().toString();

        if(email.length()>0 && nickname.length()>0 && pwd.length()>0 && pwd_check.length()>0 && number.length()>0) {
            if(pwd.length()>=6) {
                if(pwd.equals(pwd_check)) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    Log.d("user", user.toString());
                    if(user.isEmailVerified()) {
                        mAuth.confirmPasswordReset(user.getEmail(), pwd);
                        db = FirebaseFirestore.getInstance();
                        Customer_Info ci = new Customer_Info(email, nickname, number);
                        db.collection("customers").document(user.getEmail()).set(ci);
                    } else startToast("이메일 인증이 완료되지 않았습니다.");
                } else startToast("다른 비밀번호가 입력되었습니다.");
            } else startToast("비밀번호는 6자 이상 입력해주세요.");
        } else startToast("비어있는 입력이 있습니다.");
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