package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;

import com.example.mny.NoticeDialog;
import com.example.mny.View.CProfileActivity;
import com.example.mny.View.MMainActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Report implements Control {

    /* 필요 요소 */
    private String name;
    private String contents;
    private String type;

    /* 구현 상에 요구되는 요소 */
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
    public Report() {

    }
    public Report(Context context, String name, String contents, String type) {
        this.context = context;
        this.name = name;
        this.contents = contents;
        this.type = type;
    }

    /* 입력된 고객 또는 가게 이름 */
    public String getName() { return name; }

    /* 입력된 신고 내용 */
    public String getContents() { return contents; }

    /* 존재하는 고객 또는 가게 이름인가 */
    public boolean isExistName() {
        mUser = mAuth.getCurrentUser();
        Task<QuerySnapshot> task = db.collection("Info").get();
        while(true) {
            if(task.isSuccessful()) {
                for(QueryDocumentSnapshot document : task.getResult()) {
                    if(document.getData().containsKey(getName())) {
                        return true;
                    }
                }
                makeNotice("확인", "존재하지 않는 이름입니다");
                return false;
            }
        }
    }

    /* 내용이 채워졌는가 */
    public boolean isFilled() {
        if(getName().length() == 0 || getContents().length() == 0) {
            makeNotice("확인", "내용을 모두 입력해주세요");
            return false;
        } else return true;
    }

    /* 최종 신고 절차 */
    public void report() {
        boolean fill = isFilled();
        if(fill) {
            boolean exist = isExistName();
            if(exist) {
                Map<String, Object> report_info = new HashMap<>();
                report_info.put("name", getName());
                report_info.put("contents", getContents());
                db.collection("Report").document(mUser.getUid()).set(report_info);
                changePage("Main");
            }
        }
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(type.equals("Customer")) {
            intent= new Intent(context, CProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else if(type.equals("Makret")) {
            intent= new Intent(context, MMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    @Override
    public void startToast(String msg) {

    }

    @Override
    public void makeNotice(String type, String msg) {
        NoticeDialog nd = new NoticeDialog(context, type, msg);
        nd.show();
    }
}
