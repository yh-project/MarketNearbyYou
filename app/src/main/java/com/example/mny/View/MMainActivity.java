package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mny.Controller.MarketMain;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
public class MMainActivity extends AppCompatActivity{

    private ImageView back;
    private Button deliveryList;
    private Button goodsList;
    private Button addGoods;
    private RecyclerView list;
    private MarketMain marketMain;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmain);

        back = findViewById(R.id.back);
        deliveryList = findViewById(R.id.deliveryList);
        goodsList = findViewById(R.id.goodsList);
        addGoods = findViewById(R.id.addGoods);
        list = findViewById(R.id.list);

        back.setOnClickListener(onClickListener);
        deliveryList.setOnClickListener(onClickListener);
        goodsList.setOnClickListener(onClickListener);
        addGoods.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        marketMain = new MarketMain(MMainActivity.this, list);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.deliveryList:
                    marketMain.getReservedList();
                    break;
                case R.id.goodsList:
                    marketMain.getGoodsList();
                    break;
                case R.id.addGoods:
                    marketMain.changePage("Add");
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(MMainActivity.this, "앱을 종료시키겠습니까?", "종료", "취소", MMainActivity.class, this::onBackPressed, null);
        tpd.show();
    }
}