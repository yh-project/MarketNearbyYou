package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mny.Controller.ManageGoods;
import com.example.mny.Controller.UpdateGoods;
import com.example.mny.Model.Goods;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddGoodsActivity extends AppCompatActivity {

    private EditText input_name;
    private EditText input_price;
    private EditText input_maxcount;
    private Spinner input_currentStock;
    private Spinner input_goodstype;
    private Button update;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Goods pickedGoods;
    private UpdateGoods updateGoods;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);

        input_name = findViewById(R.id.input_name);
        input_price = findViewById(R.id.input_price);
        input_maxcount = findViewById(R.id.input_maxcount);
        input_currentStock = findViewById(R.id.input_currentstock);
        input_goodstype = findViewById(R.id.input_goodstype);
        update = findViewById(R.id.update);
        back = findViewById(R.id.back);

        ArrayAdapter<CharSequence> cs = ArrayAdapter.createFromResource(this, R.array.CurrentStock, android.R.layout.simple_spinner_item);
        cs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_currentStock.setAdapter(cs);
        ArrayAdapter<CharSequence> gt = ArrayAdapter.createFromResource(this, R.array.GoodsType, android.R.layout.simple_spinner_item);
        gt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_goodstype.setAdapter(gt);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        updateGoods = new UpdateGoods(AddGoodsActivity.this);
        Intent intent = getIntent();
        if((pickedGoods = (Goods)intent.getSerializableExtra("goods")) != null) {
            updateGoods.setChangeOrnew(false);
            updateGoods.setGoodsInfo(pickedGoods);
            input_name.setText(pickedGoods.getName());
            input_price.setText(Integer.toString(pickedGoods.getPrice()));
            input_maxcount.setText(Integer.toString(pickedGoods.getMax()));
            for(int i=0; i<cs.getCount(); i++) {
                if(cs.getItem(i).equals(pickedGoods.getCurrentStock())) {
                    input_currentStock.setSelection(i);
                    break;
                }
            }

            for(int i=0; i<gt.getCount(); i++) {
                if(gt.getItem(i).equals(pickedGoods.getCategory())) {
                    input_goodstype.setSelection(i);
                    break;
                }
            }
        } else {
            updateGoods.setChangeOrnew(true);
        }
        update.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.update:
                    Log.d("sibla", "??");
                    Goods after = new Goods();
                    after.setName(input_name.getText().toString());
                    after.setCategory(input_goodstype.getSelectedItem().toString());
                    after.setCurrentStock(input_currentStock.getSelectedItem().toString());
                    if(input_price.getText().toString().equals("")) after.setPrice(-1);
                    else after.setPrice(Integer.parseInt(input_price.getText().toString()));
                    if(input_maxcount.getText().toString().equals("")) after.setMax(-1);
                    else after.setMax(Integer.parseInt(input_maxcount.getText().toString()));

                    if(updateGoods.isChangeOrnew()) updateGoods.addGoods(after);
                    else updateGoods.changeGoods(after);
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(AddGoodsActivity.this, "메인 화면으로\n이동하시겠습니까?", "확인", "취소", ManageGoodsActivity.class, null);
        tpd.show();
    }
}