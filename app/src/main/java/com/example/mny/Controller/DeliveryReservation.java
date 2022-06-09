package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.Customer;
import com.example.mny.Model.DeliveryData;
import com.example.mny.NoticeDialog;
import com.example.mny.View.CMainActivity;
import com.example.mny.View.DeliveryReservationAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DeliveryReservation implements Control {

    private ArrayList<DeliveryData> timeList = new ArrayList<>();
    private String pickedTime;
    private boolean isPicked;
    private String marketName;
    private String type;

    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView tList;
    private DeliveryReservationAdapter deliveryReservationAdapter;

    public DeliveryReservation() {

    }

    public DeliveryReservation(Context context) {
        this.context = context;
    }
    public DeliveryReservation(Context context, RecyclerView tList, String type) {
        this.context = context;
        this.tList = tList;
        this.type = type;
    }

    public void getTimeList() {
        mUser = mAuth.getCurrentUser();
        db.collection("Delivery").document(getMarketName())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<Map.Entry<String, Object>> tmp = new ArrayList<>(documentSnapshot.getData().entrySet());
                for(int i=0; i<tmp.size(); i++) {
                    DeliveryData deliveryData = new DeliveryData();
                    deliveryData.setTime(tmp.get(i).getKey());
                    deliveryData.setNickname(tmp.get(i).getValue().toString());
                    timeList.add(deliveryData);
                    timeList.sort(new Comparator<DeliveryData>() {
                        @Override
                        public int compare(DeliveryData d1, DeliveryData d2) {
                            String[] d1arr = d1.getTime().split(":");
                            String[] d2arr = d2.getTime().split(":");
                            if(d1arr[0].equals(d2arr[0])) {
                                if(Integer.parseInt(d1arr[1]) < Integer.parseInt(d2arr[1])) return -1;
                                else if(Integer.parseInt(d1arr[1]) > Integer.parseInt(d2arr[1])) return 1;
                                else return 0;
                            } else {
                                if(Integer.parseInt(d1arr[0]) < Integer.parseInt(d2arr[0])) return -1;
                                else return 1;
                            }
                        }
                    });
                }
                showList();
            }
        });

    }
    public void setTimeList(ArrayList<DeliveryData> timeList) { this.timeList = timeList; }

    public String getPickedTime() { return pickedTime; }
    public void setPickedTime(String pickedTime) { this.pickedTime = pickedTime; }

    public boolean isPicked() { return isPicked; }
    public void setPicked(boolean picked) { isPicked = picked; }

    public String getMarketName() { return marketName; }
    public void setMarketName(String marketName) { this.marketName = marketName; }

    public void reserve() {
        mUser = mAuth.getCurrentUser();
        db.collection("Delivery").document(getMarketName())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> tmp = documentSnapshot.getData();
                String nickname;
                db.collection("Customer").document(mUser.getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        if(tmp.containsValue(customer.getNickname())) {
                            List<Map.Entry<String, Object>> entry = new ArrayList<>(tmp.entrySet());
                            for(int i=0; i<entry.size(); i++) {
                                if(entry.get(i).getValue().equals(customer.getNickname())) {
                                    String time = entry.get(i).getKey();
                                    tmp.put(time, "");
                                }
                            }
                        }
                        tmp.put(getPickedTime(), customer.getNickname());
                        db.collection("Delivery").document(getMarketName()).set(tmp);
                        DeliveryData deliveryData = new DeliveryData();
                        deliveryData.setTime(getPickedTime());
                        deliveryData.setNickname(customer.getNickname());
                        db.collection("Customer").document(mUser.getUid()).collection("Delivery").document(getMarketName()).set(deliveryData);
                    }
                });
            }
        });
        changePage("Main");
    }

    public void showList() {
        deliveryReservationAdapter = new DeliveryReservationAdapter(timeList);
        tList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        tList.setAdapter(deliveryReservationAdapter);
    }

    public void choiceTime() {
        if(deliveryReservationAdapter.getPickedList().size() == 0) makeNotice("확인", "시간을 선택해주세요");
        else if(deliveryReservationAdapter.getPickedList().size() > 1) makeNotice("확인", "한개만 선택가능합니다");
        else {
            setPickedTime(deliveryReservationAdapter.getPickedList().get(0));
            reserve();
        }
    }

    @Override
    public void changePage(String pageName) {
        Intent intent;
        intent = new Intent(context, CMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
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
