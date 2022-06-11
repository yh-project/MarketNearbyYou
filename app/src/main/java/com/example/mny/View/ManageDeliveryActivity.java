package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.mny.Controller.ManageMarketReservation;
import com.example.mny.Model.DeliveryData;
import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ManageDeliveryActivity extends AppCompatActivity {

    private TextView nickname;
    private TextView number;
    private TextView time;
    private Button cancel;
    private Button change;
    private DeliveryData deliveryData;
    private Market market;
    private ManageMarketReservation manageMarketReservation;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managedelivery);

        nickname = findViewById(R.id.nickname);
        number = findViewById(R.id.number);
        time = findViewById(R.id.time);
        cancel = findViewById(R.id.cancel);
        change = findViewById(R.id.change);

        cancel.setOnClickListener(onClickListener);
        change.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Intent intent = getIntent();
        deliveryData = (DeliveryData)intent.getSerializableExtra("Delivery");
        mUser = mAuth.getCurrentUser();
        Task<QuerySnapshot> task = db.collection("Customer").get();
        while(true) {
            if(task.isSuccessful()) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if(queryDocumentSnapshot.getData().containsValue(deliveryData.getNickname())) {
                        number.setText(queryDocumentSnapshot.getData().get("number").toString());
                        nickname.setText(deliveryData.getNickname());
                        time.setText(deliveryData.getTime());
                    }
                }
                break;
            }
        }
        Task<DocumentSnapshot> tasks = db.collection("Market").document(mUser.getUid()).get();
        while(true) {
            if(tasks.isSuccessful()) {
                market = tasks.getResult().toObject(Market.class);
                break;
            }
        }
        manageMarketReservation = new ManageMarketReservation(ManageDeliveryActivity.this, market);
        manageMarketReservation.setDeliveryData(deliveryData);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.cancel:
                    manageMarketReservation.cancelReservation();
                    break;
                case R.id.change:
                    manageMarketReservation.changeReservation();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(ManageDeliveryActivity.this, "메인 화면으로\n이동하시겠습니까?", "확인", "취소", MMainActivity.class, null, null);
        tpd.show();
    }
}