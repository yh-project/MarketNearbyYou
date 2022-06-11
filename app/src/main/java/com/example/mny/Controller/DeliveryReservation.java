package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.Customer;
import com.example.mny.Model.DeliveryData;
import com.example.mny.NoticeDialog;
import com.example.mny.View.CMainActivity;
import com.example.mny.View.DeliveryReservationAdapter;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DeliveryReservation implements Control {

    /* 필요 요소 */
    private ArrayList<DeliveryData> timeList = new ArrayList<>();
    private String pickedTime;
    private String marketName;
    private String type;
    private String target;

    /* 구현 상에 요구되는 요소 */
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView tList;
    private DeliveryReservationAdapter deliveryReservationAdapter;

    /* 생성자 */
    public DeliveryReservation() {

    }
    public DeliveryReservation(Context context) {
        this.context = context;
    }
    public DeliveryReservation(Context context, RecyclerView tList, String type, String target) {
        this.context = context;
        this.tList = tList;
        this.type = type;
        this.target = target;
    }

    /* 배달 시간 목록 */
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

    /* 선택된 시간 getter, setter */
    public String getPickedTime() { return pickedTime; }
    public void setPickedTime(String pickedTime) { this.pickedTime = pickedTime; }

    /* 선택된 가게 이름 getter, setter */
    public String getMarketName() { return marketName; }
    public void setMarketName(String marketName) { this.marketName = marketName; }

    /* 배달 예약 */
    public void reserve() {
        mUser = mAuth.getCurrentUser();
        if(type.equals("Customer")) {
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
        } else if(type.equals("Market")) {
            db.collection("Delivery").document(getMarketName())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> tmp = documentSnapshot.getData();
                    db.collection("Customer").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        Customer customer;
                                        for(QueryDocumentSnapshot document : task.getResult()) {
                                            customer = document.toObject(Customer.class);
                                            if (customer.getNickname().equals(target)) {
                                                if (tmp.containsValue(target)) {
                                                    List<Map.Entry<String, Object>> entry = new ArrayList<>(tmp.entrySet());
                                                    for (int i = 0; i < entry.size(); i++) {
                                                        if (entry.get(i).getValue().equals(target)) {
                                                            String time = entry.get(i).getKey();
                                                            tmp.put(time, "");
                                                        }
                                                    }
                                                }
                                                tmp.put(getPickedTime(), target);
                                                db.collection("Delivery").document(getMarketName()).set(tmp);
                                                DeliveryData deliveryData = new DeliveryData();
                                                deliveryData.setTime(getPickedTime());
                                                deliveryData.setNickname(target);
                                                db.collection("Customer").document(document.getId()).collection("Delivery").document(getMarketName()).set(deliveryData);
                                            }
                                        }
                                    }
                                }
                            });
                }
            });
            changePage("Main");
        }
    }

    /* 배달 시간 목록 디스플레이 */
    public void showList() {
        deliveryReservationAdapter = new DeliveryReservationAdapter(timeList, type, target);
        tList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        tList.setAdapter(deliveryReservationAdapter);
    }

    /* 시간 선택 */
    public void choiceTime() {
        if(deliveryReservationAdapter.getPickedList().size() == 0) makeNotice("확인", "시간을 선택해주세요");
        else if(deliveryReservationAdapter.getPickedList().size() > 1) makeNotice("확인", "한개만 선택가능합니다");
        else {
            setPickedTime(deliveryReservationAdapter.getPickedList().get(0));
            reserve();
        }
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(type.equals("Customer")) {
            intent = new Intent(context, CMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else if(type.equals("Market")) {
            intent = new Intent(context, MMainActivity.class);
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
}
