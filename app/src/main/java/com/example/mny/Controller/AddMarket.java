package com.example.mny.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mny.Model.Market;
import com.example.mny.NoticeDialog;
import com.example.mny.View.AddMarketActivity2;
import com.example.mny.View.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMarket implements Control {

    /* 필요 요소 */
    private Market user;
    private String type;

    /* 구현 상의 요구되는 요소 */
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
    public AddMarket() {

    }

    public AddMarket(Context context) {
        this.context = context;
    }

    /* 유저 타입 지정 */
    public void setType(String type) { this.type = type; }

    /* 입력된 정보의 Market 생성 */
    public void setMarket(Market user) { this.user = user; }

    /* 인증 메일 전송 */
    public void sendMail(String email, Button button) {
        if(email.length() == 0) { startToast("이메일을 입력해주세요"); }
        else {
            mAuth.createUserWithEmailAndPassword(email, "123456")
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mUser = mAuth.getCurrentUser();
                                mUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                startToast("인증메일을 전송했습니다");
                                                button.setClickable(false);
                                            }
                                        });
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    startToast("중복된 이메일입니다");
                                }
                            }
                        }
                    });
        }
    }

    /* 메일 인증 확인 && 기본적인 정보 저장 */
    public void isCheckedMail(String pwd, String check) {
        mUser.reload();
        if(mUser.isEmailVerified()) {
            if(user.getNumber().length() == 0 || user.getMarketname().length() == 0) startToast("모든 내용을 입력해주세요");
            else if(pwd.length() < 6 || check.length() < 6) startToast("비밀번호는 6자 이상입니다");
            else if(!pwd.equals(check)) startToast("서로 다른 비밀번호입니다");
            else {
                DocumentReference documentReference = db.collection("Info").document("storename");
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Map<String, Object> nameList = documentSnapshot.getData();
                            Map<String, Object> typeList = new HashMap<>();
                            typeList.put("type", type);
                            if (nameList.containsKey(user.getMarketname())) {
                                startToast("중복된 가게명입니다");
                            } else {
                                mUser.updatePassword(pwd)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    db.collection("Users").document(mUser.getUid()).set(typeList);
                                                    db.collection("Market").document(mUser.getUid()).set(user);
                                                    nameList.put(user.getMarketname(), user.getMarketname());
                                                    db.collection("Info").document("storename").update(nameList);
                                                    changePage("Detail");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
            }
        } else {
            startToast("이메일 인증을 완료해주세요");
        }
    }

    /* 최종 회원가입 절차 진행 */
    public void signup() {
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        if(user.getMarketType() == 0 || user.getAddress_detail().length() == 0) startToast("모든 내용을 입력해주세요");
        else {
            db.collection("Market").document(mUser.getUid()).set(user);
            Map<String, Object> dmap = new HashMap<>();
            String start = user.getStart();
            String finish = user.getFinish();
            int sm;
            if(start.equals(finish)) {
                dmap.put(start, "");
            } else {
                int term;
                if(user.getTerm().equals("30분")) term = 30;
                else term = 0;
                String[] sarr = start.split(":");
                String[] farr = finish.split(":");
                while(!sarr[0].equals(farr[0]) || !sarr[1].equals(farr[1])) {
                    dmap.put(sarr[0]+":"+sarr[1], "");
                    if(term == 0) sarr[0] = Integer.toString(Integer.parseInt(sarr[0])+1);
                    else {
                        sm = Integer.parseInt(sarr[1]) + 30;
                        if(sm == 60) {
                            sarr[1] = "00";
                            sarr[0] = Integer.toString(Integer.parseInt(sarr[0])+1);
                        } else if(sm == 30) {
                            sarr[1] = Integer.toString(sm);
                        }
                    }
                }
            }
            db.collection("Delivery").document(user.getMarketname()).set(dmap);
            startToast("가게 등록에 성공했습니다");
            changePage("Login");
        }
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(pageName.equals("Detail")) {
            intent = new Intent(context, AddMarketActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
        else if(pageName.equals("Login")) {
            intent = new Intent(context, LoginActivity.class);
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
