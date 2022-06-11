package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.mny.Model.Customer;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cprofile);

        TextView nickname = findViewById(R.id.nickname);
        TextView email = findViewById(R.id.email);
        TextView number = findViewById(R.id.number);
        Button MR = findViewById(R.id.MR);
        Button SB = findViewById(R.id.SB);
        Button report = findViewById(R.id.report);

        MR.setOnClickListener(onClickListener);
        SB.setOnClickListener(onClickListener);
        report.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        mUser = mAuth.getCurrentUser();
        Task<DocumentSnapshot> task = db.collection("Customer").document(mUser.getUid()).get();
        while(true) {
            if(task.isSuccessful()) {
                customer = task.getResult().toObject(Customer.class);
                nickname.setText(customer.getNickname());
                email.setText(customer.getEmail());
                number.setText(customer.getNumber());
                break;
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch(v.getId()) {
                case R.id.MR:
                    intent = new Intent(CProfileActivity.this, ManageReservationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.SB:
                    intent = new Intent(CProfileActivity.this, ShoppingBasketActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.report:
                    intent = new Intent(CProfileActivity.this, ReportActivity.class);
                    intent.putExtra("type", "Customer");
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
        TwoPickDialog tpd = new TwoPickDialog(CProfileActivity.this, "메인화면으로 이동하시겠습니까?", "확인", "취소", CMainActivity.class, null, null);
        tpd.show();
    }
}