package com.example.mny.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.CustomerGoods;
import com.example.mny.Model.Goods;
import com.example.mny.Model.Market;
import com.example.mny.NoticeDialog;
import com.example.mny.R;
import com.example.mny.SBDialog;
import com.example.mny.View.CMainAdapter;
import com.example.mny.View.PickMarketActivity;
import com.example.mny.View.PickMarketAdapter;
import com.example.mny.View.ShoppingBasketActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerMain implements Control, CMainAdapter.onListListener, SBDialog.SBClickListener {

    private ArrayList<CustomerGoods> goodsList = new ArrayList<CustomerGoods>();
    private Market selectedMarket = new Market();
    private String selectedGoods;

    private Context context;
    private RecyclerView goods_List;
    private CMainAdapter cMainAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CustomerMain() {

    }

    public CustomerMain(Context context) {
        this.context = context;
    }
    public CustomerMain(Context context, Market selectedMarket, RecyclerView goods_List) {
        this.context = context;
        this.selectedMarket = selectedMarket;
        this.goods_List = goods_List;
    }

    public String getSelectedGoods() { return this.selectedGoods; }
    public void setSelectedGoods(String customerGoods) { this.selectedGoods = customerGoods; }

    public void getList() {
        mUser = mAuth.getCurrentUser();
        db.collection(selectedMarket.getMarketname()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    db.collection("Customer").document(mUser.getUid()).collection("Reserve").document(getSelectedMarket().getMarketname())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> tasks) {
                            Map<String, Object> tmp = new HashMap<>();
                            tmp = tasks.getResult().getData();
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                CustomerGoods customerGoods = queryDocumentSnapshot.toObject(CustomerGoods.class);
                                if(tmp.containsKey(customerGoods.getName())) {
                                    customerGoods.setStatus(true);
                                    tmp.put(customerGoods.getName(), customerGoods.getCurrentStock());
                                    db.collection("Customer").document(mUser.getUid()).collection("Reserve").document(getSelectedMarket().getMarketname()).set(tmp);
                                }
                                goodsList.add(customerGoods);
                            }
                            if(goodsList.size() == 0) {
                                makeNotice("확인", "상품이 없습니다");
                                changeText();
                            } else showList();
                        }
                    });
                } else changeText();
            }
        });
    }
    public void setList(ArrayList<CustomerGoods> goodsList) { this.goodsList = goodsList; }

    public void showList() {
        cMainAdapter = new CMainAdapter(goodsList, this);
        goods_List.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        goods_List.setAdapter(cMainAdapter);
    }

    public void pickGoodsCategory(String category, String currentStock) {
        if(selectedMarket.getMarketType() == 0) startToast("선택된 가게가 없습니다");
        else {
            mUser = mAuth.getCurrentUser();
            goodsList.clear();
            db.collection(selectedMarket.getMarketname()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(category.equals(queryDocumentSnapshot.get("category")) && currentStock.equals(queryDocumentSnapshot.get("currentStock"))) {
                                CustomerGoods customerGoods = queryDocumentSnapshot.toObject(CustomerGoods.class);
                                goodsList.add(customerGoods);
                            }
                        }
                        if(goodsList.size() == 0) makeNotice("확인", "상품이 없습니다");
                        else showList();
                    } else makeNotice("확인", "상품이 없습니다");
                }
            });
        }
    }

    public void changeText() {
        ((TextView) ((Activity)context).findViewById(R.id.noGoods)).setVisibility(View.VISIBLE);
    }

    public void putShoppingBasket(String name) {
        db.collection(selectedMarket.getMarketname()).document(name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        setSelectedGoods(documentSnapshot.toObject(Goods.class).getName());
                        changePage("SBWithData");
                    }
                });
    }

    public void reserveGoods(String name, String currentStock, String isReserved) {
        mUser = mAuth.getCurrentUser();
        if(!currentStock.equals("재고 없음")) makeNotice("확인", "예약할 수 없는 상품입니다");
        else if(isReserved.equals("예약 완료")) startToast("이미 예약된 상품입니다");
        else {
            DocumentReference documentReference = db.collection("Customer").document(mUser.getUid());
            documentReference.collection("Reserve").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                Map<String, Object> tmp = new HashMap<>();
                                if(task.getResult().size() == 0) {
                                    tmp.put(name, currentStock);
                                } else {
                                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        if(queryDocumentSnapshot.getId().equals(getSelectedMarket().getMarketname())) {
                                            tmp = queryDocumentSnapshot.getData();
                                            if(!tmp.containsKey(name)) tmp.put(name, currentStock);
                                            break;
                                        }
                                    }
                                }
                                documentReference.collection("Reserve").document(getSelectedMarket().getMarketname()).set(tmp);
                            }
                        }
                    });
            startToast("예약되었습니다");
        }
    }

    public Market getSelectedMarket() { return selectedMarket; }
    public void setSelectedMarket(Market selectedMarket) { this.selectedMarket = selectedMarket; }

    @Override
    public void changePage(String pageName) {
        Intent intent;
        if(pageName.equals("PickMarket")) {
            intent = new Intent(context, PickMarketActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else if(pageName.equals("SBWithData")) {
            intent = new Intent(context, ShoppingBasketActivity.class);
            String content = selectedMarket.getMarketname() + " " + getSelectedGoods();
            intent.putExtra("newGoods", content);
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
    public void onItemSelected(String name, String price, String currentStock) {
        if(currentStock.equals("재고 없음")) makeNotice("확인", "재고가 없습니다");
        else {
            SBDialog sbDialog = new SBDialog(context, name, price, currentStock, this);
            sbDialog.show();
        }
    }

    @Override
    public void GRlistener(String name, String currentStock, String isReserved) {
        reserveGoods(name, currentStock, isReserved);
    }

    @Override
    public void OnClicked(String name) {
        putShoppingBasket(name);
    }
}
