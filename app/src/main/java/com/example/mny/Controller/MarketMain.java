package com.example.mny.Controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.DeliveryData;
import com.example.mny.Model.Goods;
import com.example.mny.Model.Market;
import com.example.mny.NoticeDialog;
import com.example.mny.View.ManageDeliveryListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MarketMain implements Control {

    private ArrayList<DeliveryData> reservedList = new ArrayList<>();
    private ArrayList<Goods> goodsList;

    private Context context;
    private RecyclerView list;
    private ManageDeliveryListAdapter manageDeliveryListAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MarketMain() {

    }

    public MarketMain(Context context, RecyclerView list) {
        this.context = context;
        this.list = list;
    }

    public void getReservedList() {
        mUser = mAuth.getCurrentUser();
        db.collection("Market").document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Market market = documentSnapshot.toObject(Market.class);
                db.collection("Delivery").document(market.getMarketname())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            int cnt = 0;
                            List<Map.Entry<String, Object>> entry = new ArrayList<>(document.getData().entrySet());
                            entry.sort(new Comparator<Map.Entry<String, Object>>() {
                                @Override
                                public int compare(Map.Entry<String, Object> e1, Map.Entry<String, Object> e2) {
                                    String[] e1arr = e1.getKey().split(":");
                                    String[] e2arr = e2.getKey().split(":");
                                    if(e1arr[0].equals(e2arr[0])) {
                                        if(Integer.parseInt(e1arr[1]) < Integer.parseInt(e2arr[1])) return -1;
                                        else if(Integer.parseInt(e1arr[1]) > Integer.parseInt(e2arr[1])) return 1;
                                        else return 0;
                                    } else {
                                        if(Integer.parseInt(e1arr[0]) < Integer.parseInt(e2arr[0])) return -1;
                                        else return 1;
                                    }
                                }
                            });
                            for(int i=0; i<entry.size(); i++) {
                                DeliveryData deliveryData = new DeliveryData();
                                deliveryData.setTime(entry.get(i).getKey());
                                deliveryData.setNickname(entry.get(i).getValue().toString());
                                if(deliveryData.getNickname().equals("")) cnt++;
                                reservedList.add(deliveryData);
                            }
                            if(cnt == entry.size()) makeNotice("확인", "예약된 일정이 없습니다.");
                            else showDeliveryList();
                        }
                    }
                });
            }
        });
    }
    public void setReservedList(ArrayList<DeliveryData> reservedList) { this.reservedList = reservedList; }
    public void showDeliveryList() {
        manageDeliveryListAdapter = new ManageDeliveryListAdapter(reservedList, context);
        list.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        list.setAdapter(manageDeliveryListAdapter);
    }

    public ArrayList<Goods> getGoodsList() { return goodsList; }
    public void setGoodsList(ArrayList<Goods> goodsList) { this.goodsList = goodsList; }
    public void showGoodsList() {

    }

    @Override
    public void changePage(String pageName) {

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