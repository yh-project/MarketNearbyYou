package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mny.CategoryDialog;
import com.example.mny.Controller.CustomerMain;
import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

import java.util.ArrayList;

public class CMainActivity extends AppCompatActivity {
    private CategoryDialog cd;
    private RecyclerView goodsList;
    /*private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<String> stocks;*/
    private Market market;
    private CustomerMain customerMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmain);

        Button pickMarket = findViewById(R.id.pickMarket);
        Button pickCategory = findViewById(R.id.pickCategory);
        ImageView back = findViewById(R.id.back);
        goodsList = findViewById(R.id.goodsList);

        pickMarket.setOnClickListener(onClickListener);
        pickCategory.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        /*goodsList = findViewById(R.id.goodsList);
        names = new ArrayList<>();
        prices = new ArrayList<>();
        stocks = new ArrayList<>();
        for(int i=0; i<10; i++) {
            names.add("과자" + i);
            prices.add((i*100) + "원");
            stocks.add("재고 많음");
        }*/
        /*goodsList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        goodsList.setAdapter(cMainAdapter);*/

        Intent intent = getIntent();
        if((market = (Market)intent.getSerializableExtra("market")) != null) {
            Log.d("성공", market.getMarketname());
            customerMain = new CustomerMain(CMainActivity.this, market, goodsList);
            customerMain.getList();
        } else {
            Log.d("실패", "받을 것 없음");
            customerMain = new CustomerMain(CMainActivity.this);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.pickMarket:
                    customerMain.changePage("PickMarket");
                    break;
                case R.id.pickCategory:
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(CMainActivity.this, "앱을 종료시키겠습니까?", "종료", "취소", CMainActivity.class);
        tpd.show();
    }
}