package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.NoticeDialog;
import com.example.mny.TwoPickDialog;
import com.example.mny.View.CProfileActivity;
import com.example.mny.View.DeliveryReservationActivity;
import com.example.mny.View.ManageReservationActivity;
import com.example.mny.View.ReservedGoodsAdapter;
import com.example.mny.View.ReservedMarketsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ManageCustomerReservation implements Control, TwoPickDialog.RemoveClickLister, ReservedGoodsAdapter.GoodsListener {

    private Map<String, Map<String, Object>> reservedGoodsList = new HashMap<>();

    private Context context;
    private RecyclerView reservedList;
    private TextView marketName;

    private TextView time;
    private ReservedMarketsAdapter reservedMarketsAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ManageCustomerReservation() {

    }

    public ManageCustomerReservation(Context context, RecyclerView reservedList) {
        this.context = context;
        this.reservedList = reservedList;
    }

    public void getReservedmarket(TextView name) {
        mUser = mAuth.getCurrentUser();
        db.collection("Customer").document(mUser.getUid()).collection("Delivery")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    String market = "예약 없음";
                    for (QueryDocumentSnapshot documet : task.getResult()) {
                        market = documet.getId();
                    }
                    name.setText(market);
                }
            }
        });
    }

    public void getReservedTime(TextView time) {
        mUser = mAuth.getCurrentUser();
        db.collection("Customer").document(mUser.getUid()).collection("Delivery")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    String times = "예약 없음";
                    for (QueryDocumentSnapshot documet : task.getResult()) {
                        times = documet.getData().get("time").toString();
                    }
                    time.setText(times);
                }
            }
        });
    }

    public void getReservedGoodsList() {
        mUser = mAuth.getCurrentUser();
        reservedGoodsList.clear();
        db.collection("Customer").document(mUser.getUid()).collection("Reserve")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot documet : task.getResult()) {
                        if(documet.getData().size() == 0) {
                            db.collection("Customer").document(mUser.getUid()).collection("Reserve").document(documet.getId()).delete();
                        }
                        else reservedGoodsList.put(documet.getId(), documet.getData());
                    }
                    //db.collection("Customer").document(mUser.getUid()).collection("Reserve").document(documet.getId()).delete();
                    showList();
                }
            }
        });
    }
    public void setReservedGoodsList(Map<String, Map<String, Object>> reservedGoodsList) { this.reservedGoodsList = reservedGoodsList; }

    public void cancelDeliveryReservation(TextView marketName, TextView time) {
        TwoPickDialog td = new TwoPickDialog(context, "정말 삭제하시겠습니까?", "삭제", "취소", ManageReservationActivity.class, this);
        td.show();
        this.marketName = marketName;
        this.time = time;
    }

    public void changeDeliveryTime(TextView marketName, TextView time) {
        this.marketName = marketName;
        this.time = time;
        changePage("Delivery");
    }

    public void showList() {
        reservedMarketsAdapter = new ReservedMarketsAdapter(reservedGoodsList, this);
        reservedList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        reservedList.setAdapter(reservedMarketsAdapter);
    }

    public void cancelGoodsReservation(String marketName, String name) {
        makeNotice("확인", "상품을 삭제하였습니다");
        mUser = mAuth.getCurrentUser();
        Map<String, Object> tmp = new HashMap<>();
        tmp.put(name, FieldValue.delete());
        db.collection("Customer").document(mUser.getUid()).collection("Reserve").document(marketName).update(tmp);
        getReservedGoodsList();
    }

    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(pageName.equals("Delivery")) {
            intent = new Intent(context, DeliveryReservationActivity.class);
            intent.putExtra("name", marketName.getText().toString() + "///Customer/// ");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else if(pageName.equals("Profile")) {
            intent = new Intent(context, CProfileActivity.class);
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

    @Override
    public void Deliveryremovelistener() {
        mUser = mAuth.getCurrentUser();
        db.collection("Delivery").document(marketName.getText().toString())
                .update(time.getText().toString(), "");
        db.collection("Customer").document(mUser.getUid()).collection("Delivery")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                }
            }
        });
        marketName.setText("예약 없음");
        time.setText("예약 없음");
    }

    @Override
    public void RemoveListener(String marketName, String name) {
        cancelGoodsReservation(marketName, name);
    }
}
