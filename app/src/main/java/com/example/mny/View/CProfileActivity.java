package com.example.mny.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mny.CategoryDialog;
import com.example.mny.Controller.CustomerMain;
import com.example.mny.Model.Customer;
import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cprofile);

        TextView nickname = findViewById(R.id.nickname);
        TextView email = findViewById(R.id.email);
        TextView number = findViewById(R.id.number);
        Button MR = findViewById(R.id.MR);
        Button SB = findViewById(R.id.SB);
        Button editprofile = findViewById(R.id.editprofile);
        Button report = findViewById(R.id.report);

        MR.setOnClickListener(onClickListener);
        SB.setOnClickListener(onClickListener);
        editprofile.setOnClickListener(onClickListener);
        report.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        mUser = mAuth.getCurrentUser();
        db.collection("Customer").document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Customer customer = documentSnapshot.toObject(Customer.class);
                nickname.setText(customer.getNickname());
                email.setText(customer.getEmail());
                number.setText(customer.getNumber());
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.MR:
                    break;
                case R.id.SB:
                    Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.editprofile:
                    break;
                case R.id.report:
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(CProfileActivity.this, "앱을 종료시키겠습니까?", "종료", "취소", CProfileActivity.class);
        tpd.show();
    }
}