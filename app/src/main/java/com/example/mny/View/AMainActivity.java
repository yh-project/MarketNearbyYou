package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mny.Controller.AdministratorMain;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
public class AMainActivity extends AppCompatActivity{

    private ImageView back;
    private RecyclerView userList;
    private Button customerUser;
    private Button marketUser;
    private String type;
    private AdministratorMain administratorMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amain);

        back = findViewById(R.id.back);
        userList = findViewById(R.id.userList);
        customerUser = findViewById(R.id.customerUser);
        marketUser = findViewById(R.id.marketUser);

        back.setOnClickListener(onClickListener);
        customerUser.setOnClickListener(onClickListener);
        marketUser.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.customerUser:
                    type = "Customer";
                    administratorMain = new AdministratorMain(AMainActivity.this, type, userList);
                    administratorMain.getList();
                    break;
                case R.id.marketUser:
                    type = "Market";
                    administratorMain = new AdministratorMain(AMainActivity.this, type, userList);
                    administratorMain.getList();
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(AMainActivity.this, "앱을 종료시키겠습니까?", "종료", "취소", AMainActivity.class, null, null);
        tpd.show();
    }
}