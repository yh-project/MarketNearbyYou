package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mny.Controller.PickMarket;
import com.example.mny.NoticeDialog;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class PickMarketActivity extends AppCompatActivity {

    PickMarket pickMarket;
    CheckBox mart;
    CheckBox convenience;
    CheckBox andsome;
    Spinner seedo;
    Spinner seegungoo;
    int marketType = 0;

    private NoticeDialog ad;
    private RecyclerView market_list;
    private PickMarketAdapter pickMarketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickmarket);

        Button searchMarket = findViewById(R.id.searchMarket);
        searchMarket.setOnClickListener(onClickListener);

        market_list = findViewById(R.id.market_list);
        mart = findViewById(R.id.mart);
        convenience = findViewById(R.id.convenience);
        andsome = findViewById(R.id.andsome);
        seedo = findViewById(R.id.seedo);
        seegungoo = findViewById(R.id.seegungoo);
        TextView tv = findViewById(R.id.selected_address);

        mart.setOnClickListener(onClickListener);
        convenience.setOnClickListener(onClickListener);
        andsome.setOnClickListener(onClickListener);

        ArrayAdapter<CharSequence> sd = ArrayAdapter.createFromResource(this, R.array.seedo, android.R.layout.simple_spinner_item);
        sd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seedo.setAdapter(sd);

        seedo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView1, View view, int position1, long id) {
                tv.setText(adapterView1.getItemAtPosition(position1).toString());
                int ids = 0;
                switch(tv.getText().toString()) {
                    case "???????????????":
                        ids = R.array.Seoul;break;
                    case "???????????????":
                        ids = R.array.Daegu;break;
                    case "???????????????":
                        ids = R.array.Seoul;break;
                    case "???????????????":
                        ids = R.array.Daegu;break;
                    case "???????????????":
                        ids = R.array.Seoul;break;
                    case "???????????????":
                        ids = R.array.Daegu;break;
                    case "???????????????":
                        ids = R.array.Seoul;break;
                    case "?????????????????????":
                        ids = R.array.Daegu;break;
                    case "?????????":
                        ids = R.array.Seoul;break;
                    case "?????????":
                        ids = R.array.Daegu;break;
                    case "????????????":
                        ids = R.array.Seoul;break;
                    case "????????????":
                        ids = R.array.Daegu;break;
                    case "????????????":
                        ids = R.array.Seoul;break;
                    case "????????????":
                        ids = R.array.Daegu;break;
                    case "????????????":
                        ids = R.array.Seoul;break;
                    case "????????????":
                        ids = R.array.Daegu;break;
                    case "?????????????????????":
                        ids = R.array.Daegu;break;
                }
                ArrayAdapter<CharSequence> sgg = ArrayAdapter.createFromResource(PickMarketActivity.this, ids, android.R.layout.simple_spinner_item);
                sgg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                seegungoo.setAdapter(sgg);

                seegungoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        tv.setText(adapterView1.getItemAtPosition(position1).toString() + " " + adapterView.getItemAtPosition(position).toString());
                    }
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.mart:
                    convenience.setChecked(false);
                    andsome.setChecked(false);
                    marketType = 1;
                    break;
                case R.id.convenience:
                    mart.setChecked(false);
                    andsome.setChecked(false);
                    marketType = 2;
                    break;
                case R.id.andsome:
                    mart.setChecked(false);
                    convenience.setChecked(false);
                    marketType = 3;
                    break;
                case R.id.searchMarket:
                    pickMarket = new PickMarket(PickMarketActivity.this, market_list);
                    if(!mart.isChecked() && !convenience.isChecked() && !andsome.isChecked()) {
                        marketType = 0;
                        pickMarket.startToast("?????? ????????? ??????????????????");
                    } else {
                        pickMarket.getList(marketType, ((TextView)findViewById(R.id.selected_address)).getText().toString());
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(PickMarketActivity.this, "????????? ????????? ???????????????.", "??????", "??????", CMainActivity.class, null, null);
        tpd.show();
    }
}