package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.mny.Controller.DeliveryReservation;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;


public class DeliveryReservationActivity extends AppCompatActivity {

    private String marketName = "";
    private DeliveryReservation deliveryReservation;

    private RecyclerView deliveryList;
    private Button reserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryreservation);

        deliveryList = findViewById(R.id.deliveryList);
        reserve = findViewById(R.id.reserve);

        reserve.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Intent intent = getIntent();
        if(!(marketName = intent.getStringExtra("name")).equals("")) {
            deliveryReservation = new DeliveryReservation(DeliveryReservationActivity.this, deliveryList);
            deliveryReservation.setMarketName(marketName);
            deliveryReservation.getTimeList();
        } else {
            deliveryReservation = new DeliveryReservation(DeliveryReservationActivity.this);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.reserve:
                    deliveryReservation.choiceTime();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(DeliveryReservationActivity.this, "메인 화면으로 돌아가시겠습니까?", "확인", "취소", CMainActivity.class);
        tpd.show();
    }
}