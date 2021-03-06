package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mny.CategoryDialog;
import com.example.mny.Controller.CustomerMain;
import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class CMainActivity extends AppCompatActivity implements CategoryDialog.OnClickListener {
    private RecyclerView goodsList;
    private Market market;
    private CustomerMain customerMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmain);

        Button pickMarket = findViewById(R.id.pickMarket);
        Button pickCategory = findViewById(R.id.pickCategory);
        ImageView back = findViewById(R.id.back);
        ImageView profile = findViewById(R.id.profile);
        goodsList = findViewById(R.id.goodsList);

        pickMarket.setOnClickListener(onClickListener);
        pickCategory.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        profile.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Intent intent = getIntent();
        if((market = (Market)intent.getSerializableExtra("market")) != null) {
            customerMain = new CustomerMain(CMainActivity.this, market, goodsList);
            customerMain.getList();
        } else {
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
                    CategoryDialog cd = new CategoryDialog(CMainActivity.this, CMainActivity.this);
                    cd.show();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
                case R.id.profile:
                    customerMain.changePage("Profile");
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(CMainActivity.this, "?????? ?????????????????????????", "??????", "??????", null, null, null);
        tpd.show();
    }

    @Override
    public void OnClicked(String category, String currentStock) {
        customerMain.pickGoodsCategory(category, currentStock);
    }
}