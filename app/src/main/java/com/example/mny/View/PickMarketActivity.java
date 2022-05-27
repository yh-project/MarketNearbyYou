package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mny.NoticeDialog;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;

public class PickMarketActivity extends AppCompatActivity {
    private NoticeDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickmarket);

        Button searchMarket = findViewById(R.id.searchMarket);
        searchMarket.setOnClickListener(onClickListener);

        Spinner seedo = findViewById(R.id.seedo);
        Spinner seegungoo = findViewById(R.id.seegungoo);
        TextView tv = findViewById(R.id.selected_address);

        ArrayAdapter<CharSequence> sd = ArrayAdapter.createFromResource(this, R.array.seedo, android.R.layout.simple_spinner_item);
        sd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seedo.setAdapter(sd);

        seedo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView1, View view, int position1, long id) {
                tv.setText(adapterView1.getItemAtPosition(position1).toString());
                int ids = 0;
                switch(tv.getText().toString()) {
                    case "서울특별시":
                        ids = R.array.Seoul;break;
                    case "대구광역시":
                        ids = R.array.Daegu;break;
                    case "부산광역시":
                        ids = R.array.Seoul;break;
                    case "인천광역시":
                        ids = R.array.Daegu;break;
                    case "광주광역시":
                        ids = R.array.Seoul;break;
                    case "대전광역시":
                        ids = R.array.Daegu;break;
                    case "울산광역시":
                        ids = R.array.Seoul;break;
                    case "세종특별자치시":
                        ids = R.array.Daegu;break;
                    case "경기도":
                        ids = R.array.Seoul;break;
                    case "강원도":
                        ids = R.array.Daegu;break;
                    case "충청북도":
                        ids = R.array.Seoul;break;
                    case "충청남도":
                        ids = R.array.Daegu;break;
                    case "전라북도":
                        ids = R.array.Seoul;break;
                    case "전라남도":
                        ids = R.array.Daegu;break;
                    case "경상북도":
                        ids = R.array.Seoul;break;
                    case "경상남도":
                        ids = R.array.Daegu;break;
                    case "제주특별자치도":
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
                case R.id.searchMarket:
                    //ad = new NoticeDialog(PickMarketActivity.this, "해당 가게에\n등록된 상품이 없습니다.");
                    //ad.show();
                    /*startToast("종류 또는 위치를 지정해주세요.");*/
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(PickMarketActivity.this, "입력한 내용이 사라집니다.", "확인", "취소");
        tpd.show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}