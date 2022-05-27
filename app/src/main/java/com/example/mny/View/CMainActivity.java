package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.mny.CategoryDialog;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

import java.util.ArrayList;

public class CMainActivity extends AppCompatActivity {
    private CategoryDialog cd;
    private RecyclerView goodsList;
    private CMainAdapter cMainAdapter;
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<String> stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmain);

        Button pickMarket = findViewById(R.id.pickMarket);
        Button pickCategory = findViewById(R.id.pickCategory);

        pickMarket.setOnClickListener(onClickListener);
        pickCategory.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        goodsList = findViewById(R.id.goodsList);
        names = new ArrayList<>();
        prices = new ArrayList<>();
        stocks = new ArrayList<>();
        for(int i=0; i<10; i++) {
            names.add("과자" + i);
            prices.add((i*100) + "원");
            stocks.add("재고 많음");
        }
        cMainAdapter = new CMainAdapter(names, prices, stocks);
        goodsList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        goodsList.setAdapter(cMainAdapter);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.pickMarket:
                    startActivity(PickMarketActivity.class);
                    break;
                case R.id.pickCategory:
                    cd = new CategoryDialog(CMainActivity.this);
                    cd.show();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(CMainActivity.this, "앱을 종료시키겠습니까?", "종료", "취소");
        tpd.show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}