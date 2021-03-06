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
    private String type = "";
    private String target = "";

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
            String[] contents = marketName.split("///");
            marketName = contents[0];
            type = contents[1];
            target = contents[2];
            deliveryReservation = new DeliveryReservation(DeliveryReservationActivity.this, deliveryList, type, target);
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
        TwoPickDialog tpd = new TwoPickDialog(DeliveryReservationActivity.this, "?????? ???????????? ?????????????????????????", "??????", "??????", CMainActivity.class, this::onBackPressed, null);
        tpd.show();
    }
}