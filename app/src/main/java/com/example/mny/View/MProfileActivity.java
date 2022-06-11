package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Market market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mprofile);

        TextView email = findViewById(R.id.email);
        TextView name = findViewById(R.id.name);
        TextView number = findViewById(R.id.number);
        TextView time = findViewById(R.id.time);
        TextView deliveryTime = findViewById(R.id.deliveryTime);
        TextView type = findViewById(R.id.type);
        TextView address = findViewById(R.id.address);
        Button report = findViewById(R.id.report);
        ImageView back = findViewById(R.id.back);

        report.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        mUser = mAuth.getCurrentUser();
        Task<DocumentSnapshot> task = db.collection("Market").document(mUser.getUid()).get();
        while(true) {
            if(task.isSuccessful()) {
                market = task.getResult().toObject(Market.class);
                email.setText(market.getEmail());
                name.setText(market.getMarketname());
                number.setText(market.getNumber());
                time.setText(market.getOpen() + " ~ " + market.getClose());
                deliveryTime.setText(market.getStart() + " ~ " + market.getFinish());
                if(market.getMarketType() == 1) type.setText("마트");
                else if(market.getMarketType() == 2) type.setText("편의점");
                else type.setText("그 외");
                address.setText(market.getAddress());
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch(v.getId()) {
                case R.id.report:
                    intent = new Intent(MProfileActivity.this, ReportActivity.class);
                    intent.putExtra("type", "Market");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(MProfileActivity.this, "앱을 종료시키겠습니까?", "종료", "취소", MProfileActivity.class, null, null);
        tpd.show();
    }
}