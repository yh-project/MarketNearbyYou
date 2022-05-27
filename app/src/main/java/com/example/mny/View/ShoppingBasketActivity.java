package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mny.R;

import java.util.ArrayList;

public class ShoppingBasketActivity extends AppCompatActivity {
    private RecyclerView sbList;
    private SBGoodsAdapter sbGoodsAdapter;
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<String> stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingbasket);

        ImageView back = findViewById(R.id.back);

        names = new ArrayList<>();
        prices = new ArrayList<>();
        stocks = new ArrayList<>();
        for(int i=0; i<10; i++) {
            names.add("과자" + i+1);
            prices.add((i*100) + "원");
            stocks.add(i+1 + "");
        }

        sbList = findViewById(R.id.sbList);
        sbGoodsAdapter = new SBGoodsAdapter(names, prices, stocks);
        sbList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        sbList.setAdapter(sbGoodsAdapter);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
            }
        }
    };

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}