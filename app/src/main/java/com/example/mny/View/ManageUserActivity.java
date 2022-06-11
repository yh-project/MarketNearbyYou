package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mny.Controller.ManageUsers;
import com.example.mny.Model.Customer;
import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
public class ManageUserActivity extends AppCompatActivity{

    private ImageView back;
    private Customer customer;
    private Market market;
    private String type;
    private ManageUsers manageUsers;

    private ConstraintLayout customView;
    private EditText cnickname;
    private EditText cnumber;
    private EditText cemail;
    private Button cthree;
    private Button cforever;
    private Button cfinish;

    private ConstraintLayout marketView;
    private EditText mname;
    private EditText mnumber;
    private EditText memail;
    private Button mthree;
    private Button mforever;
    private Button mfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageuser);

        back = findViewById(R.id.back);

        back.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Intent intent = getIntent();
        if((type = intent.getStringExtra("type")) != null) {
            if(type.equals("Customer")) {
                customer = (Customer)intent.getSerializableExtra("user");
                customView = findViewById(R.id.customView);
                cnickname = findViewById(R.id.cnickname);
                cnumber = findViewById(R.id.cnumber);
                cemail = findViewById(R.id.cemail);
                cthree = findViewById(R.id.cthree);
                cforever = findViewById(R.id.cforever);
                cfinish = findViewById(R.id.cfinish);
                marketView = findViewById(R.id.marketView);

                customView.setVisibility(View.VISIBLE);
                marketView.setVisibility(View.INVISIBLE);
                cthree.setOnClickListener(onClickListener);
                cforever.setOnClickListener(onClickListener);
                cfinish.setOnClickListener(onClickListener);

                cnickname.setText(customer.getNickname());
                cnumber.setText(customer.getNumber());
                cemail.setText(customer.getEmail());

                manageUsers = new ManageUsers(ManageUserActivity.this, type);
                manageUsers.setC(customer);
            }
            else if(type.equals("Market")) {
                market = (Market)intent.getSerializableExtra("user");
                marketView = findViewById(R.id.marketView);
                mname = findViewById(R.id.mname);
                mnumber = findViewById(R.id.mnumber);
                memail = findViewById(R.id.memail);
                mthree = findViewById(R.id.mthree);
                mforever = findViewById(R.id.mforever);
                mfinish = findViewById(R.id.mfinish);
                customView = findViewById(R.id.customView);

                marketView.setVisibility(View.VISIBLE);
                customView.setVisibility(View.INVISIBLE);
                mthree.setOnClickListener(onClickListener);
                mforever.setOnClickListener(onClickListener);
                mfinish.setOnClickListener(onClickListener);

                mname.setText(market.getMarketname());
                mnumber.setText(market.getNumber());
                memail.setText(market.getEmail());

                manageUsers = new ManageUsers(ManageUserActivity.this, type);
                manageUsers.setM(market);
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.cthree:
                    manageUsers.threeBan();
                    break;
                case R.id.cforever:
                    manageUsers.permanentBan();
                    break;
                case R.id.mthree:
                    manageUsers.threeBan();
                    break;
                case R.id.mforever:
                    manageUsers.permanentBan();
                    break;
                case R.id.cfinish:
                    Customer afterC = new Customer();
                    afterC.setEmail(cemail.getText().toString());
                    afterC.setNumber(cnumber.getText().toString());
                    afterC.setNickname(cnickname.getText().toString());
                    afterC.setBanCount(customer.getBanCount());
                    afterC.setBanned(customer.getisBanned());
                    manageUsers.changeInfo(afterC, null);
                    break;
                case R.id.mfinish:
                    Market afterM = new Market();
                    afterM.setEmail(memail.getText().toString());
                    afterM.setNumber(mnumber.getText().toString());
                    afterM.setMarketname(mname.getText().toString());
                    afterM.setBanCount(market.getBanCount());
                    afterM.setBanned(market.getisBanned());
                    afterM.setAddress(market.getAddress());
                    afterM.setAddress_detail(market.getAddress_detail());
                    afterM.setClose(market.getClose());
                    afterM.setFinish(market.getFinish());
                    afterM.setMarketType(market.getMarketType());
                    afterM.setOpen(market.getOpen());
                    afterM.setStart(market.getStart());
                    afterM.setTerm(market.getTerm());
                    manageUsers.changeInfo(null, afterM);
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(ManageUserActivity.this, "메인화면으로 돌아가시겠습니까?", "확인", "취소", AMainActivity.class, null, null);
        tpd.show();
    }
}