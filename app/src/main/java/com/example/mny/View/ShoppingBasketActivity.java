package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mny.Controller.ShoppingBasket;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;


public class ShoppingBasketActivity extends AppCompatActivity {
    private RecyclerView sbList;
    private String selectedMG;
    private ShoppingBasket shoppingBasket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingbasket);

        Button deliveryReserve = findViewById(R.id.deliveryReserve);
        ImageView back = findViewById(R.id.back);
        sbList = findViewById(R.id.sbList);

        deliveryReserve.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Intent intent = getIntent();
        if((selectedMG = intent.getStringExtra("newGoods")) != null) {
            shoppingBasket = new ShoppingBasket(ShoppingBasketActivity.this, selectedMG, sbList);
            shoppingBasket.getSBList();
        } else {
            shoppingBasket = new ShoppingBasket(ShoppingBasketActivity.this, sbList);
            shoppingBasket.getSBList();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.deliveryReserve:
                    shoppingBasket.chooseMarket();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(ShoppingBasketActivity.this, "메인 화면으로 돌아가시겠습니까?", "확인", "취소", CMainActivity.class);
        tpd.show();
    }
}