package com.example.mny.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mny.Controller.AddMarket;
import com.example.mny.Model.Market;
import com.example.mny.R;
import com.example.mny.TwoPickDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddMarketActivity2 extends AppCompatActivity {

    private AddMarket addMarket2_control;
    private Market user;
    private int marketType;

    private Button addmarket;
    private CheckBox mart;
    private CheckBox convenience;
    private CheckBox andsome;
    private Spinner seedo;
    private Spinner seegungoo;
    private Spinner fromO;
    private Spinner toO;
    private Spinner from;
    private Spinner tos;
    private Spinner duration;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmarket2);

        addmarket = findViewById(R.id.addMarket);
        mart = findViewById(R.id.mart);
        convenience = findViewById(R.id.convenience);
        andsome = findViewById(R.id.andsome);
        seedo = findViewById(R.id.seedo);
        seegungoo = findViewById(R.id.seegungoo);
        fromO = findViewById(R.id.fromO);
        toO = findViewById(R.id.toO);
        from = findViewById(R.id.from);
        tos = findViewById(R.id.to);
        duration = findViewById(R.id.duration);

        addmarket.setOnClickListener(onClickListener);
        mart.setOnClickListener(onClickListener);
        convenience.setOnClickListener(onClickListener);
        andsome.setOnClickListener(onClickListener);

        TextView tv = findViewById(R.id.selected_address);

        ArrayAdapter<CharSequence> fo = ArrayAdapter.createFromResource(this, R.array.RunTime, android.R.layout.simple_spinner_item);
        fo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromO.setAdapter(fo);
        ArrayAdapter<CharSequence> to = ArrayAdapter.createFromResource(this, R.array.RunTime, android.R.layout.simple_spinner_item);
        to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toO.setAdapter(to);

        ArrayAdapter<CharSequence> froms = ArrayAdapter.createFromResource(this, R.array.RunTime, android.R.layout.simple_spinner_item);
        froms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(froms);
        ArrayAdapter<CharSequence> toss = ArrayAdapter.createFromResource(this, R.array.RunTime, android.R.layout.simple_spinner_item);
        toss.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tos.setAdapter(toss);
        ArrayAdapter<CharSequence> durations = ArrayAdapter.createFromResource(this, R.array.Duration, android.R.layout.simple_spinner_item);
        durations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(durations);

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
                        ids = R.array.Busan;break;
                    case "???????????????":
                        ids = R.array.Incheon;break;
                    case "???????????????":
                        ids = R.array.Kwangju;break;
                    case "???????????????":
                        ids = R.array.Daejeon;break;
                    case "???????????????":
                        ids = R.array.Ulsan;break;
                    case "?????????????????????":
                        ids = R.array.Saejong;break;
                    case "?????????":
                        ids = R.array.KKD;break;
                    case "?????????":
                        ids = R.array.KWD;break;
                    case "????????????":
                        ids = R.array.CBD;break;
                    case "????????????":
                        ids = R.array.CND;break;
                    case "????????????":
                        ids = R.array.JBD;break;
                    case "????????????":
                        ids = R.array.JND;break;
                    case "????????????":
                        ids = R.array.KBD;break;
                    case "????????????":
                        ids = R.array.KND;break;
                    case "?????????????????????":
                        ids = R.array.JJD;break;
                }
                ArrayAdapter<CharSequence> sgg = ArrayAdapter.createFromResource(AddMarketActivity2.this, ids, android.R.layout.simple_spinner_item);
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

        mUser = mAuth.getCurrentUser();
        DocumentReference documentReference = db.collection("Market").document(mUser.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(Market.class);
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
                case R.id.addMarket:
                    addMarket2_control = new AddMarket(AddMarketActivity2.this);
                    if(!mart.isChecked() && !convenience.isChecked() && !andsome.isChecked()) {
                        marketType = 0;
                        addMarket2_control.startToast("?????? ????????? ??????????????????");
                    } else {
                        user.setMarketType(marketType);
                        user.setAddress(((TextView)findViewById(R.id.selected_address)).getText().toString());
                        user.setAddress_detail(((EditText)findViewById(R.id.input_detail)).getText().toString());
                        user.setOpen(fromO.getSelectedItem().toString());
                        user.setClose(toO.getSelectedItem().toString());
                        user.setStart(from.getSelectedItem().toString());
                        user.setFinish(tos.getSelectedItem().toString());
                        user.setTerm(duration.getSelectedItem().toString());
                        addMarket2_control.setMarket(user);
                        addMarket2_control.signup();
                    }
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        TwoPickDialog tpd = new TwoPickDialog(AddMarketActivity2.this, "????????? ????????? ???????????????.", "??????", "??????", LoginActivity.class, null, null);
        tpd.show();
    }
}