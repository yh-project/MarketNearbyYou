package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mny.Controller.ManageCustomerReservation;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class ManageReservationActivity extends AppCompatActivity {

    private ManageCustomerReservation manageCustomerReservation;
    private RecyclerView reservedList;
    private TextView marketName;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managereservation);

        marketName = findViewById(R.id.marketName);
        time = findViewById(R.id.deliveryTime);
        Button deleteDelivery = findViewById(R.id.deleteDelivery);
        Button changeDelivery = findViewById(R.id.changeDelivery);
        reservedList = findViewById(R.id.reservedList);

        deleteDelivery.setOnClickListener(onClickListener);
        changeDelivery.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        manageCustomerReservation = new ManageCustomerReservation(ManageReservationActivity.this, reservedList);
        manageCustomerReservation.getReservedGoodsList();
        manageCustomerReservation.getReservedmarket(marketName);
        manageCustomerReservation.getReservedTime(time);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.deleteDelivery:
                    if(marketName.getText().toString().equals("?????? ??????")) Toast.makeText(ManageReservationActivity.this, "????????? ????????? ????????????", Toast.LENGTH_SHORT).show();
                    else manageCustomerReservation.cancelDeliveryReservation(marketName, time);
                    break;
                case R.id.changeDelivery:
                    if(marketName.getText().toString().equals("?????? ??????")) Toast.makeText(ManageReservationActivity.this, "????????? ????????? ????????????", Toast.LENGTH_SHORT).show();
                    else manageCustomerReservation.changeDeliveryTime(marketName, time);
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(ManageReservationActivity.this, "????????? ????????????\n?????????????????????????", "??????", "??????", CProfileActivity.class, null, null);
        tpd.show();
    }
}