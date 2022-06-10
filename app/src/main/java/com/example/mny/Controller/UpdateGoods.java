package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mny.Model.Goods;
import com.example.mny.Model.Market;
import com.example.mny.View.MMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdateGoods implements Control {

    private boolean changeOrnew;
    private Goods goodsInfo;

    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UpdateGoods() {

    }

    public UpdateGoods(Context context) {
        this.context = context;
    }

    public boolean isChangeOrnew() { return changeOrnew; }
    public void setChangeOrnew(boolean changeOrnew) { this.changeOrnew = changeOrnew; }

    public Goods getGoodsInfo() { return goodsInfo; }
    public void setGoodsInfo(Goods goodsInfo) { this.goodsInfo = goodsInfo; }

    public void changeGoods(Goods newInfo) {
        if(newInfo.getName().equals("") || newInfo.getCurrentStock().equals("") || newInfo.getCategory().equals("") || newInfo.getMax() == -1 || newInfo.getPrice() == -1) {
            Log.d("hello", "error");
            startToast("모든 항목을 입력해주세요");
        } else if(newInfo.getMax() < 0 || newInfo.getPrice() < 0) {
            Log.d("hello", "errors");
            startToast("비정상적인 수치가 입력되었습니다");
        } else {
            mUser = mAuth.getCurrentUser();
            db.collection("Market").document(mUser.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Market market = documentSnapshot.toObject(Market.class);
                            db.collection(market.getMarketname()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("hello", goodsInfo.getName());
                                                    if(document.getId().equals(goodsInfo.getName())) {
                                                        document.getReference().delete();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });
                            db.collection(market.getMarketname()).document(newInfo.getName()).set(newInfo);
                        }
                    });
            changePage("Main");
        }
    }

    public void addGoods(Goods newInfo) {
        if(newInfo.getName().equals("") || newInfo.getCurrentStock().equals("") || newInfo.getCategory().equals("") || newInfo.getMax() == -1 || newInfo.getPrice() == -1) {
            Log.d("hello", "error");
            startToast("모든 항목을 입력해주세요");
        } else if(newInfo.getMax() < 0 || newInfo.getPrice() < 0) {
            Log.d("hello", "errors");
            startToast("비정상적인 수치가 입력되었습니다");
        } else {
            mUser = mAuth.getCurrentUser();
            db.collection("Market").document(mUser.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Market market = documentSnapshot.toObject(Market.class);
                            db.collection(market.getMarketname()).document(newInfo.getName()).set(newInfo);
                        }
                    });
            changePage("Main");
        }
    }

    @Override
    public void changePage(String pageName) {
        Intent intent;
        intent = new Intent(context, MMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void startToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeNotice(String type, String msg) {

    }
}
