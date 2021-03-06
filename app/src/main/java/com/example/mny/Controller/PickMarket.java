package com.example.mny.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.Market;
import com.example.mny.NoticeDialog;
import com.example.mny.View.CMainActivity;
import com.example.mny.View.PickMarketAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PickMarket implements Control, PickMarketAdapter.OnListItemSelectedInterface {

    /* 필요 요소 */
    private ArrayList<Market> marketList = new ArrayList<Market>();
    private Market selectedMarket;
    private Context context;
    private RecyclerView market_list;
    private PickMarketAdapter pickMarketAdapter;

    /* 구현 상의 요구되는 요소 */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /* 생성자 */
    public PickMarket() {

    }

    public PickMarket(Context context, RecyclerView market_list) {
        this.context = context;
        this.market_list = market_list;
    }

    /* 가게 목록 */
    public void getList(int type, String address) {
        marketList.clear();
        db.collection("Market")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                Map<String, Object> marketMap = new HashMap<String, Object>();
                                marketMap = queryDocumentSnapshot.getData();
                                if((""+type).equals(marketMap.get("marketType").toString()) && marketMap.containsValue(address)) {
                                    marketList.add(queryDocumentSnapshot.toObject(Market.class));
                                }
                            }
                            if(marketList.size() == 0) makeNotice("확인", "가게가 없습니다");
                            else showList();
                        }
                    }
                });
    }

    /* 목록 디스플레이 */
    public void showList() {
        pickMarketAdapter = new PickMarketAdapter(marketList, this);
        market_list.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        market_list.setAdapter(pickMarketAdapter);
    }

    /* Control 인터페이스 구현 부 */
    @Override
    public void changePage(String pageName) {
        Intent intent = new Intent(context, CMainActivity.class);
        intent.putExtra("market", selectedMarket);
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

    /* 어댑터 버튼 클릭 리스너 구현 부 */
    @Override
    public void onItemSelected(View v, int position) {
        PickMarketAdapter.MarketsHolder marketsHolder = (PickMarketAdapter.MarketsHolder)market_list.findViewHolderForAdapterPosition(position);
        selectedMarket = marketList.get(marketsHolder.getAdapterPosition());
        changePage("CMain");
    }
}
