package com.example.mny.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.CustomerGoods;
import com.example.mny.Model.Market;
import com.example.mny.NoticeDialog;
import com.example.mny.R;
import com.example.mny.View.CMainAdapter;
import com.example.mny.View.PickMarketActivity;
import com.example.mny.View.PickMarketAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerMain implements Control {

    private ArrayList<CustomerGoods> goodsList = new ArrayList<CustomerGoods>();
    private Market selectedMarket;

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

    public void getList() {
        mUser = mAuth.getCurrentUser();
        db.collection(selectedMarket.getMarketname()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        CustomerGoods customerGoods = queryDocumentSnapshot.toObject(CustomerGoods.class);
                        goodsList.add(customerGoods);
                    }
                    if(goodsList.size() == 0) {
                        makeNotice("확인", "상품이 없습니다");
                        changeText();
                    }
                    else showList();
                } else changeText();
            }
        });
    }
    public void setList(ArrayList<CustomerGoods> goodsList) { this.goodsList = goodsList; }

    public void showList() {
        cMainAdapter = new CMainAdapter(goodsList);
        goods_List.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        goods_List.setAdapter(cMainAdapter);
    }

    public ArrayList<CustomerGoods> pickGoodsCategory() {
        return goodsList;
    }

    public void changeText() {
        ((TextView) ((Activity)context).findViewById(R.id.noGoods)).setVisibility(View.VISIBLE);
    }

    public void putShoppingBasket() {

    }

    public void reserveGoods() {

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
