package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.NoticeDialog;
import com.example.mny.View.CMainActivity;
import com.example.mny.View.DeliveryReservationActivity;
import com.example.mny.View.SBGoodsAdapter;
import com.example.mny.View.SBMarketAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingBasket implements Control, SBGoodsAdapter.ManageListener {

    /* 필요 요소 */
    private Map<String, Map<String, Object>> sb = new HashMap<String, Map<String, Object>>();
    private String selectedMG = "";

    /* 구형 상에 요구되는 요소 */
    private Context context;
    private RecyclerView sbList;
    private SBMarketAdapter sbMarketAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
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

    /* 장바구니 목록 */
    public void getSBList() {
        sb.clear();
        String[] arr = getSelectedMG().split(" ");
        mUser = mAuth.getCurrentUser();
        db.collection("Customer").document(mUser.getUid()).collection("SB")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> goods = new HashMap<>();
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
                            goods = queryDocumentSnapshot.getData();
                            if(arr.length >= 2) {
                                if(queryDocumentSnapshot.getId().equals(arr[0]) && queryDocumentSnapshot.getData().containsKey(arr[1])) {
                                    goods.put(arr[1], queryDocumentSnapshot.getData().get(arr[1]));
                                    makeNotice("확인", "이미 추가된 상품입니다");
                                } else {
                                    goods.put(arr[1], 1);
                                }
                                sb.put(queryDocumentSnapshot.getId(), goods);
                                db.collection("Customer").document(mUser.getUid()).collection("SB").document(queryDocumentSnapshot.getId()).set(goods);
                            } else {
                                goods = queryDocumentSnapshot.getData();
                                sb.put(queryDocumentSnapshot.getId(), goods);
                            }
                        }
                        showList(sb);
                    }
                }
            }
        });
    }

    /* 선택된 가게와 상품 */
    public String getSelectedMG() { return selectedMG; }

    /* 장바구니 상품 목록 디스플레이 */
    public void showList(Map<String, Map<String, Object>> sb) {
        sbMarketAdapter = new SBMarketAdapter(sb, this);
        sbList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        sbList.setAdapter(sbMarketAdapter);
    }

    /* 상품 삭제 */
    public void deleteGoods(String marketName, int position) {
        mUser = mAuth.getCurrentUser();
        sb.clear();
        db.collection("Customer").document(mUser.getUid()).collection("SB").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Map.Entry<String, Object>> entry;
                            String goodsName = "";
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(marketName)) {
                                    entry = new ArrayList<>(document.getData().entrySet());
                                    goodsName = entry.get(position).getKey();
                                }
                                sb.put(document.getId(), document.getData());
                            }
                            sb.get(marketName).remove(goodsName);
                            showList(sb);
                            db.collection("Customer").document(mUser.getUid()).collection("SB").document(marketName).set(sb.get(marketName));
                        }
                    }
                });
        makeNotice("확인", "삭제되었습니다");
    }

    /* 갯수 +1 */
    public void upCount(String marketName, int position) {
        mUser = mAuth.getCurrentUser();
        Map<String, Map<String, Object>> tmp = new HashMap<>();
        sb.clear();
        db.collection(marketName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                sb.put(document.getId(), document.getData());
                            }
                            db.collection("Customer").document(mUser.getUid()).collection("SB").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            String goodsName = "";
                                            int count = 0;
                                            List<Map.Entry<String, Object>> entry;
                                            if(task.isSuccessful()) {
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    tmp.put(document.getId(), document.getData());
                                                    if(document.getId().equals(marketName)) {
                                                        entry = new ArrayList<>(document.getData().entrySet());
                                                        goodsName = entry.get(position).getKey();
                                                        count = Integer.parseInt(entry.get(position).getValue().toString());
                                                    }
                                                }
                                                if(sb.get(goodsName).get("currentStock").equals("재고 없음")) makeNotice("확인", "상품이 매진되었습니다");
                                                else if(Integer.parseInt(sb.get(goodsName).get("max").toString()) < count+1) makeNotice("확인", "최대 구매 수를 초과하였습니다");
                                                else {
                                                    tmp.get(marketName).put(goodsName, count+1);
                                                    showList(tmp);
                                                    db.collection("Customer").document(mUser.getUid()).collection("SB").document(marketName).set(tmp.get(marketName));
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    /* 갯수 -1 */
    public void downCount(String marketName, int position) {
        mUser = mAuth.getCurrentUser();
        Map<String, Map<String, Object>> tmp = new HashMap<>();
        sb.clear();
        db.collection(marketName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                sb.put(document.getId(), document.getData());
                            }
                            db.collection("Customer").document(mUser.getUid()).collection("SB").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            String goodsName = "";
                                            int count = 0;
                                            List<Map.Entry<String, Object>> entry;
                                            if(task.isSuccessful()) {
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    tmp.put(document.getId(), document.getData());
                                                    if(document.getId().equals(marketName)) {
                                                        entry = new ArrayList<>(document.getData().entrySet());
                                                        goodsName = entry.get(position).getKey();
                                                        count = Integer.parseInt(entry.get(position).getValue().toString());
                                                    }
                                                }
                                                if(sb.get(goodsName).get("currentStock").equals("재고 없음")) makeNotice("확인", "상품이 매진되었습니다");
                                                else if(count-1 <= 0) makeNotice("확인", "더이상 뺄 수 없습니다");
                                                else if(Integer.parseInt(sb.get(goodsName).get("max").toString()) < count-1) {
                                                    tmp.get(marketName).put(goodsName, Integer.parseInt(sb.get(goodsName).get("max").toString()));
                                                    makeNotice("확인", "최대 구매 수를 초과하였습니다");
                                                    showList(tmp);
                                                    db.collection("Customer").document(mUser.getUid()).collection("SB").document(marketName).set(tmp.get(marketName));
                                                }
                                                else {
                                                    tmp.get(marketName).put(goodsName, count-1);
                                                    showList(tmp);
                                                    db.collection("Customer").document(mUser.getUid()).collection("SB").document(marketName).set(tmp.get(marketName));
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    /* 배달 예약 */
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

    /* 배달예약 가게 선택 */
    public void chooseMarket() {
        if(sbMarketAdapter.mSize() == 0) makeNotice("확인", "선택된 가게가 없습니다");
        else if(sbMarketAdapter.mSize() >= 2) makeNotice("확인", "한번에 하나의 가게만 배달 가능합니다");
        else reserveDelivery(sbMarketAdapter.getMList().get(0));
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(pageName.equals("Main")) {
            intent = new Intent(context, CMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            intent = new Intent(context, DeliveryReservationActivity.class);
            intent.putExtra("name", pageName + "///Customer");
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

    /* 각종 버튼 클릭 리스너 구현 부 */
    @Override
    public void IncreaseListener(String marketName, int position) {
        upCount(marketName, position);
    }

    @Override
    public void ReduceListener(String marketName, int position) {
        downCount(marketName, position);
    }

    @Override
    public void RemoveListener(String marketName, int position) {
        deleteGoods(marketName, position);
    }
}
