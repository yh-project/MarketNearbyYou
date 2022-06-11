package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.mny.Model.Customer;
import com.example.mny.Model.Market;
import com.example.mny.NoticeDialog;
import com.example.mny.TwoPickDialog;
import com.example.mny.View.AMainActivity;
import com.example.mny.View.ManageUserActivity;
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
import java.util.List;
import java.util.Map;

public class ManageUsers implements Control, TwoPickDialog.BanClickListener {

    /* 필요 요소 */
    private Customer customer;
    private Market market;
    private String type;

    /* 구현 상에 요구되는 요소 */
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
    public ManageUsers() {

    }
    public ManageUsers(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    /* Customer, Market 설정 */
    public void setC(Customer customer) { this.customer = customer; }
    public void setM(Market makret) { this.market = makret; }

    /* 3일 정지 */
    public void threeBan() {
        TwoPickDialog td = new TwoPickDialog(context, "정지 누적 횟수 : " + customer.getBanCount() + "\n3일간 정지됩니다.", "3일 정지", "취소", ManageUserActivity.class, null, this);
        td.show();
    }

    /* 영구 정지 */
    public void permanentBan() {
        TwoPickDialog td = new TwoPickDialog(context, "영구정지됩니다.", "영구 정지", "취소", ManageUserActivity.class, null, this);
        td.show();
    }

    /* 정보 변경 */
    public void changeInfo(Customer afterC, Market afterM) {
        if(type.equals("Customer")) {
            if(afterC.getNickname().length() == 0 || afterC.getNumber().length() == 0 || afterC.getEmail().length() == 0) makeNotice("확인", "비어있는 정보가 있습니다.");
            else {
                db.collection("Customer").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        if(queryDocumentSnapshot.getData().containsValue(customer.getNickname())) {
                                            db.collection("Customer").document(queryDocumentSnapshot.getId()).update("nickname", afterC.getNickname());
                                            db.collection("Customer").document(queryDocumentSnapshot.getId()).update("number", afterC.getNumber());
                                            db.collection("Customer").document(queryDocumentSnapshot.getId()).update("email", afterC.getEmail());
                                            db.collection("Info").document("nickname").get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                            Map<String, Object> tmp = documentSnapshot.getData();
                                                            tmp.put(afterC.getNickname(), afterC.getNickname());
                                                            db.collection("Info").document("nickname").update(tmp);
                                                            tmp.put(customer.getNickname(), FieldValue.delete());
                                                            db.collection("Info").document("nickname").update(tmp);
                                                            changePage("AMain");
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        });
            }
        } else if(type.equals("Market")) {
            if(afterM.getMarketname().length() == 0 || afterM.getNumber().length() == 0 || afterM.getEmail().length() == 0) makeNotice("확인", "비어있는 정보가 있습니다.");
            else {
                db.collection("Market").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        if(queryDocumentSnapshot.getData().containsValue(market.getMarketname())) {
                                            db.collection("Market").document(queryDocumentSnapshot.getId()).update("marketname", afterM.getMarketname());
                                            db.collection("Market").document(queryDocumentSnapshot.getId()).update("number", afterM.getNumber());
                                            db.collection("Market").document(queryDocumentSnapshot.getId()).update("email", afterM.getEmail());
                                            db.collection("Info").document("storename").get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                            Map<String, Object> tmp = documentSnapshot.getData();
                                                            tmp.put(afterM.getMarketname(), afterM.getMarketname());
                                                            db.collection("Info").document("storename").update(tmp);
                                                            tmp.put(market.getMarketname(), FieldValue.delete());
                                                            db.collection("Info").document("storename").update(tmp);
                                                        }
                                                    });
                                            db.collection("Delivery").get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()) {
                                                                Map<String, Object> tmp = new HashMap<>();
                                                                for(QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
                                                                    if(queryDocumentSnapshots.getId().equals(market.getMarketname())) {
                                                                        tmp = queryDocumentSnapshots.getData();
                                                                        queryDocumentSnapshots.getReference().delete();
                                                                        db.collection("Delivery").document(afterM.getMarketname()).set(tmp);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                            db.collection(market.getMarketname()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()) {
                                                                Map<String, Map<String, Object>> tmp = new HashMap<>();
                                                                for(QueryDocumentSnapshot queryDocumentSnapshotss : task.getResult()) {
                                                                    tmp.put(queryDocumentSnapshotss.getId(), queryDocumentSnapshotss.getData());
                                                                }
                                                                List<Map.Entry<String, Map<String, Object>>> entry = new ArrayList<>(tmp.entrySet());
                                                                for(int i=0; i<entry.size(); i++) {
                                                                    db.collection(afterM.getMarketname()).document(entry.get(i).getKey()).set(entry.get(i).getValue());
                                                                }
                                                                for(QueryDocumentSnapshot queryDocumentSnapshotss : task.getResult()) {
                                                                    queryDocumentSnapshotss.getReference().delete();
                                                                }
                                                            }
                                                        }
                                                    });
                                            db.collection("Customer").get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()) {
                                                                for(QueryDocumentSnapshot queryDocumentSnapshotsss : task.getResult()) {
                                                                    db.collection("Customer").document(queryDocumentSnapshotsss.getId()).collection("Delivery").get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if(task.isSuccessful()) {
                                                                                        Map<String, Object> tmp = new HashMap<>();
                                                                                        for(QueryDocumentSnapshot queryDocumentSnapshots1 : task.getResult()) {
                                                                                            if(queryDocumentSnapshots1.getId().equals(market.getMarketname())) {
                                                                                                tmp = queryDocumentSnapshots1.getData();
                                                                                                queryDocumentSnapshots1.getReference().delete();
                                                                                                db.collection("Customer").document(queryDocumentSnapshotsss.getId()).collection("Delivery").document(afterM.getMarketname()).set(tmp);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                    db.collection("Customer").document(queryDocumentSnapshotsss.getId()).collection("Reserve").get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if(task.isSuccessful()) {
                                                                                        Map<String, Object> tmp = new HashMap<>();
                                                                                        for(QueryDocumentSnapshot queryDocumentSnapshots1 : task.getResult()) {
                                                                                            if(queryDocumentSnapshots1.getId().equals(market.getMarketname())) {
                                                                                                tmp = queryDocumentSnapshots1.getData();
                                                                                                queryDocumentSnapshots1.getReference().delete();
                                                                                                db.collection("Customer").document(queryDocumentSnapshotsss.getId()).collection("Reserve").document(afterM.getMarketname()).set(tmp);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                    db.collection("Customer").document(queryDocumentSnapshotsss.getId()).collection("SB").get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if(task.isSuccessful()) {
                                                                                        Map<String, Object> tmp = new HashMap<>();
                                                                                        for(QueryDocumentSnapshot queryDocumentSnapshots1 : task.getResult()) {
                                                                                            if(queryDocumentSnapshots1.getId().equals(market.getMarketname())) {
                                                                                                tmp = queryDocumentSnapshots1.getData();
                                                                                                queryDocumentSnapshots1.getReference().delete();
                                                                                                db.collection("Customer").document(queryDocumentSnapshotsss.getId()).collection("SB").document(afterM.getMarketname()).set(tmp);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                    changePage("AMain");
                                }
                            }
                        });
            }
        }
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent = new Intent(context, AMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void startToast(String msg) {

    }

    @Override
    public void makeNotice(String type, String msg) {
        NoticeDialog nd = new NoticeDialog(context, type, msg);
        nd.show();
    }

    /* 각종 버튼 클릭 리스너 구현 부 */
    @Override
    public void Threelistener() {
        if(type.equals("Customer")) {
            db.collection("Customer").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    if(queryDocumentSnapshot.getData().containsValue(customer.getNickname())) {
                                        db.collection("Customer").document(queryDocumentSnapshot.getId()).update("isBanned", true);
                                        db.collection("Customer").document(queryDocumentSnapshot.getId()).update("banCount", FieldValue.increment(1));
                                    }
                                }
                            }
                        }
                    });
        } else if(type.equals("Market")) {
            db.collection("Market").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    if(queryDocumentSnapshot.getData().containsValue(market.getMarketname())) {
                                        db.collection("Market").document(queryDocumentSnapshot.getId()).update("isBanned", true);
                                        db.collection("Market").document(queryDocumentSnapshot.getId()).update("banCount", FieldValue.increment(1));
                                    }
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void Foreverlistener() {
        if(type.equals("Customer")) {
            db.collection("Customer").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    if (queryDocumentSnapshot.getData().containsValue(customer.getNickname())) {
                                        db.collection("Customer").document(queryDocumentSnapshot.getId()).update("isBanned", true);
                                        db.collection("Customer").document(queryDocumentSnapshot.getId()).update("banCount", FieldValue.increment(1));
                                    }
                                }
                            }
                        }
                    });
        } else if(type.equals("Market")) {
            db.collection("Market").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    if(queryDocumentSnapshot.getData().containsValue(market.getMarketname())) {
                                        db.collection("Market").document(queryDocumentSnapshot.getId()).update("isBanned", true);
                                        db.collection("Market").document(queryDocumentSnapshot.getId()).update("banCount", FieldValue.increment(1));
                                    }
                                }
                            }
                        }
                    });
        }
    }
}