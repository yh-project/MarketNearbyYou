package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
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

    /* 필요 요소 */
    private boolean changeOrnew;
    private Goods goodsInfo;

    /* 구현상에 요구되는 요소 */
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
    public UpdateGoods() {

    }
    public UpdateGoods(Context context) {
        this.context = context;
    }

    /* changeOrnew getter, setter */
    public boolean isChangeOrnew() { return changeOrnew; }
    public void setChangeOrnew(boolean changeOrnew) { this.changeOrnew = changeOrnew; }

    /* 상품 정보 설정 */
    public void setGoodsInfo(Goods goodsInfo) { this.goodsInfo = goodsInfo; }

    /* 상품 정보 변경 */
    public void changeGoods(Goods newInfo) {
        if(newInfo.getName().equals("") || newInfo.getCurrentStock().equals("") || newInfo.getCategory().equals("") || newInfo.getMax() == -1 || newInfo.getPrice() == -1) {
            startToast("모든 항목을 입력해주세요");
        } else if(newInfo.getMax() < 0 || newInfo.getPrice() < 0) {
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

    /* 새상품 추가 */
    public void addGoods(Goods newInfo) {
        if(newInfo.getName().equals("") || newInfo.getCurrentStock().equals("") || newInfo.getCategory().equals("") || newInfo.getMax() == -1 || newInfo.getPrice() == -1) {
            startToast("모든 항목을 입력해주세요");
        } else if(newInfo.getMax() < 0 || newInfo.getPrice() < 0) {
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

    /* Control 인터페이스 구현 부 */
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
