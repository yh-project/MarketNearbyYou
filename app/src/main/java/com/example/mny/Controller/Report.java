package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mny.NoticeDialog;
import com.example.mny.View.CMainActivity;
import com.example.mny.View.CProfileActivity;
import com.example.mny.View.MMainActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Report implements Control {

    private String name;
    private String contents;
    private String type;

    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

    public Report() {

    }

    public Report(Context context, String name, String contents, String type) {
        this.context = context;
        this.name = name;
        this.contents = contents;
        this.type = type;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

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

    public boolean isFilled() {
        if(getName().length() == 0 || getContents().length() == 0) {
            makeNotice("확인", "내용을 모두 입력해주세요");
            return false;
        } else return true;
    }

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
            } else Log.d("fail", "fail");
        } else Log.d("fail", "fail");
    }

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
