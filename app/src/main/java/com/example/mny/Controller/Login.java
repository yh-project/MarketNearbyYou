package com.example.mny.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mny.Model.Customer;
import com.example.mny.Model.Market;
import com.example.mny.Model.User;
import com.example.mny.NoticeDialog;
import com.example.mny.TwoPickDialog;
import com.example.mny.View.AddMarketActivity1;
import com.example.mny.View.CMainActivity;
import com.example.mny.View.JoinActivity;
import com.example.mny.View.MMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login implements Control{

    private String email;
    private String password;
    private String type;
    private User loginUser;

    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

    public Login(String email, String password, Context context) {
        this.email = email;
        this.password = password;
        this.context = context;
    }

    public void login() {
        if(email.length() == 0 || password.length() == 0) {
            startToast("이메일 또는 비밀번호를 입력해주세요");
        } else if(password.length() < 6) {
            startToast("비밀번호는 6자리 이상 입력해주세요");
        } else {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 로그인 성공
                                setType();
                            } else {
                                //로그인 실패
                                if (task.getException() instanceof FirebaseTooManyRequestsException) {
                                    startToast("최근 로그인 시도가 너무 많습니다.\n잠시 후 다시 시도해주세요."); //오류시도 다수
                                }
                                else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    startToast("비밀번호가 틀렸습니다."); //비밀번호 오류
                                }
                                else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                    startToast("등록된 유저가 아닙니다."); //유저 존재 X
                                }
                                else {
                                    startToast(task.getException().toString());
                                }
                            }
                        }
                    });
        }
    }

    public String getType() { return type; }
    public void setType() {
        documentReference = db.collection("Users").document(mAuth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                type = documentSnapshot.getString("type");
                getUser(type);
            }
        });

    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public void getUser(String type) {
        if(type.equals("Customer")) {
            documentReference = db.collection("Customer").document(mAuth.getUid());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    loginUser = documentSnapshot.toObject(Customer.class);
                    if(loginUser.getisBanned()) makeNotice("정지!", "정지된 유저입니다. 종료합니다");
                    else {
                        changePage("CMain");
                    }

                }
            });
        } else if(type.equals("Market")) {
            documentReference = db.collection("Market").document(mAuth.getUid());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    loginUser = documentSnapshot.toObject(Market.class);
                    if(loginUser.getisBanned()) makeNotice("정지!", "정지된 유저입니다. 종료합니다");
                    else {
                        changePage("MMain");
                    }
                }
            });
        }
    }
    public void setUser(User loginUser) { this.loginUser = loginUser; }

    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(pageName.equals("Join")) {
            intent = new Intent(context, JoinActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
        else if(pageName.equals("AddMarket")) {
            intent = new Intent(context, AddMarketActivity1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
        else if(pageName.equals("CMain")) {
            intent = new Intent(context, CMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
        else if(pageName.equals("MMain")) {
            intent = new Intent(context, MMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    @Override
    public void startToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeNotice(String type, String msg) {
        NoticeDialog nd = new NoticeDialog(context, type, msg);
        nd.show();
    }
}

