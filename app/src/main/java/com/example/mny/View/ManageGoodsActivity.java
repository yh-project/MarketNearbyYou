package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mny.Controller.ManageGoods;
import com.example.mny.Model.Goods;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageGoodsActivity extends AppCompatActivity {

    private TextView name;
    private TextView price;
    private TextView currentStock;
    private TextView category;
    private TextView maxcount;
    private Button remove;
    private Button change;
    private ImageView back;
    private Goods goods;
    private ManageGoods manageGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managegoods);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        currentStock = findViewById(R.id.currentStock);
        category = findViewById(R.id.category);
        maxcount = findViewById(R.id.maxcount);
        remove = findViewById(R.id.remove);
        change = findViewById(R.id.change);
        back = findViewById(R.id.back);

        remove.setOnClickListener(onClickListener);
        change.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Intent intent = getIntent();
        goods = (Goods)intent.getSerializableExtra("Goods");
        name.setText(goods.getName());
        price.setText(Integer.toString(goods.getPrice()));
        currentStock.setText(goods.getCurrentStock());
        category.setText(goods.getCategory());
        maxcount.setText(Integer.toString(goods.getMax()));

        manageGoods = new ManageGoods(ManageGoodsActivity.this);
        manageGoods.setPickedGoods(goods);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.remove:
                    manageGoods.deleteGoods();
                    break;
                case R.id.change:
                    manageGoods.changeGoods(0);
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(ManageGoodsActivity.this, "메인 화면으로\n이동하시겠습니까?", "확인", "취소", MMainActivity.class, null, null);
        tpd.show();
    }
}