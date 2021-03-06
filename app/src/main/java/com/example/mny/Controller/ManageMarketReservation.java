package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.mny.Model.DeliveryData;
import com.example.mny.Model.Market;
import com.example.mny.TwoPickDialog;
import com.example.mny.View.DeliveryReservationActivity;
import com.example.mny.View.ManageDeliveryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ManageMarketReservation implements Control, TwoPickDialog.RemoveClickLister {

    /* 필요 요소 */
    private DeliveryData deliveryData;
    private Market market;

    /* 구현 상에 요구되는 요소 */
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
    public ManageMarketReservation () {

    }
    public ManageMarketReservation(Context context, Market market) {
        this.context = context;
        this.market = market;
    }

    /* 배달 일정 설정 */
    public void setDeliveryData(DeliveryData deliveryData) { this.deliveryData = deliveryData; }

    /* 해당 배달 취소 */
    public void cancelReservation() {
        TwoPickDialog td = new TwoPickDialog(context, "정말 삭제하시겠습니까?", "삭제", "취소", ManageDeliveryActivity.class, this, null);
        td.show();
    }

    /* 배달 일정 변경 */
    public void changeReservation() {
        changePage("DR");
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent;
        intent = new Intent(context, DeliveryReservationActivity.class);
        intent.putExtra("name", market.getMarketname()+"///Market///" + deliveryData.getNickname());
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
        db.collection("Delivery").document(market.getMarketname())
                .update(deliveryData.getTime(), "");
        Task<QuerySnapshot> task = db.collection("Customer").get();
        while(true) {
            if(task.isSuccessful()) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if(queryDocumentSnapshot.getData().containsValue(deliveryData.getNickname())) {
                        db.collection("Customer").document(queryDocumentSnapshot.getId()).collection("Delivery").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for(QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
                                                queryDocumentSnapshots.getReference().delete();
                                            }
                                        }
                                    }
                                });
                    }
                }
                break;
            }
        }
    }
}
