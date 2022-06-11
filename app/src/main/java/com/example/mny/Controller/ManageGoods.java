package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mny.Model.Goods;
import com.example.mny.Model.Market;
import com.example.mny.TwoPickDialog;
import com.example.mny.View.AddGoodsActivity;
import com.example.mny.View.ManageGoodsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageGoods implements Control, TwoPickDialog.RemoveClickLister{

    /* 필요 요소 */
    private Goods pickedGoods;

    /* 구현 상에 요구되는 요소 */
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
    public ManageGoods() {

    }
    public ManageGoods(Context context) {
        this.context = context;
    }

    /* 선택된 상품 getter, setter */
    public Goods getPickedGoods() { return pickedGoods; }
    public void setPickedGoods(Goods pickedGoods) { this.pickedGoods = pickedGoods; }

    /* 상품 삭제 */
    public void deleteGoods() {
        TwoPickDialog td = new TwoPickDialog(context, "정말 삭제하시겠습니까?", "삭제", "취소", ManageGoodsActivity.class, this, null);
        td.show();
    }

    /* 상품 정보 변경 */
    public void changeGoods(int type) {
        changePage("Update");
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent;
        intent = new Intent(context, AddGoodsActivity.class);
        intent.putExtra("goods", getPickedGoods());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void startToast(String msg) {

    }

    @Override
    public void makeNotice(String type, String msg) {

    }

    /* 버튼 클릭 리스너 구현 부 */
    @Override
    public void Deliveryremovelistener() {
        mUser = mAuth.getCurrentUser();
        Market market;
        Task<DocumentSnapshot> mtask = db.collection("Market").document(mUser.getUid()).get();
        while(true) {
            if(mtask.isSuccessful()) {
                market = mtask.getResult().toObject(Market.class);
                db.collection(market.getMarketname()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        if(queryDocumentSnapshot.getId().equals(pickedGoods.getName())) {
                                            queryDocumentSnapshot.getReference().delete();
                                        }
                                    }
                                }
                            }
                        });
                break;
            }
        }
        Task<QuerySnapshot> ctask = db.collection("Customer").get();
        ArrayList<String> cList = new ArrayList<>();
        while(true) {
            if(ctask.isSuccessful()) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : ctask.getResult()) {
                    cList.add(queryDocumentSnapshot.getId());
                }
                break;
            }
        }
        for(int i=0; i<cList.size(); i++) {
            Map<String, Object> updates = new HashMap<>();
            updates.put(pickedGoods.getName(), FieldValue.delete());
            db.collection("Customer").document(cList.get(i)).collection("SB").document(market.getMarketname()).update(updates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()) {
                                Log.d("fail", "nogoods");
                            }
                        }
                    });
        }
        for(int i=0; i<cList.size(); i++) {
            Map<String, Object> updates = new HashMap<>();
            updates.put(pickedGoods.getName(), FieldValue.delete());
            db.collection("Customer").document(cList.get(i)).collection("Reserve").document(market.getMarketname()).update(updates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()) {
                                Log.d("fail", "nogoods");
                            }
                        }
                    });
        }
    }
}
