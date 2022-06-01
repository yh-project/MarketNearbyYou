package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.Market;
import com.example.mny.NoticeDialog;
import com.example.mny.View.CMainActivity;
import com.example.mny.View.DeliveryReservationActivity;
import com.example.mny.View.SBMarketAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket implements Control {

    private Map<String, Map<String, Object>> sb = new HashMap<String, Map<String, Object>>();
    private String selectedMG = "";

    private Context context;
    private RecyclerView sbList;
    private SBMarketAdapter sbMarketAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ShoppingBasket() {

    }

    public ShoppingBasket(Context context, RecyclerView sbList) {
        this.context = context;
        this.sbList = sbList;
    }
    public ShoppingBasket(Context context, String selectedMG, RecyclerView sbList) {
        this.context = context;
        this.selectedMG = selectedMG;
        this.sbList = sbList;
    }

    public void getSBList() {
        sb.clear();
        Map<String, Object> goods = new HashMap<>();
        String[] arr = getSelectedMG().split(" ");
        mUser = mAuth.getCurrentUser();
        db.collection("Customer").document(mUser.getUid()).collection("SB")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().size() == 0) {
                        if(arr.length >= 2) {
                            goods.put(arr[1], 1);
                            sb.put(arr[0], goods);
                            db.collection("Customer").document(mUser.getUid()).collection("SB").document(arr[0]).set(goods);
                            showList(sb);
                        } else makeNotice("확인", "장바구니가 비어있습니다");
                    } else {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(arr.length >= 2) {
                                if(queryDocumentSnapshot.getId().equals(arr[0]) && queryDocumentSnapshot.getData().containsKey(arr[1])) {
                                    makeNotice("확인", "이미 추가된 상품입니다");
                                }
                            }
                            sb.put(queryDocumentSnapshot.getId(), queryDocumentSnapshot.getData());
                        }
                        showList(sb);
                    }
                }
            }
        });
    }
    public void setSBList(Map<String, Map<String, Object>> sb) { this.sb = sb; }

    public String getSelectedMG() { return selectedMG; }
    public void setSelectedMG(String selectedMG) { this.selectedMG = selectedMG; }

    public void showList(Map<String, Map<String, Object>> sb) {
        sbMarketAdapter = new SBMarketAdapter(sb);
        sbList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        sbList.setAdapter(sbMarketAdapter);
    }

    public void deleteGoods() {

    }

    public void upCount() {

    }

    public void downCount() {

    }

    public void reserveDelivery(String marketName) {
        mUser = mAuth.getCurrentUser();
        db.collection("Customer").document(mUser.getUid()).collection("Delivery")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size()>=1) {
                    makeNotice("확인", "기존에 완료되지 않은 배달이 있습니다");
                } else {
                    String start, finish;
                    db.collection("Market").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String start, finish;
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if(queryDocumentSnapshot.getData().get("marketname").equals(marketName)) {
                                    start = queryDocumentSnapshot.getData().get("start").toString();
                                    finish = queryDocumentSnapshot.getData().get("finish").toString();
                                    if(start.equals(finish)) {
                                        makeNotice("확인", "배달을 하지 않는 가게입니다");
                                        break;
                                    } else {
                                        db.collection("Delivery").document(marketName).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        Map<String, Object> dstatus = task.getResult().getData();
                                                        if(!dstatus.containsValue("")) makeNotice("확인", "빈 시간이 없습니다");
                                                        else changePage(marketName);
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void chooseMarket() {
        if(sbMarketAdapter.mSize() == 0) makeNotice("확인", "선택된 가게가 없습니다");
        else if(sbMarketAdapter.mSize() >= 2) makeNotice("확인", "한번에 하나의 가게만 배달 가능합니다");
        else reserveDelivery(sbMarketAdapter.getMList().get(0));
    }

    public void fixInfo() {

    }

    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(pageName.equals("Main")) {
            intent = new Intent(context, CMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            intent = new Intent(context, DeliveryReservationActivity.class);
            intent.putExtra("name", pageName);
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
