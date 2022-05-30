package com.example.mny;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mny.View.PickMarketAdapter;

import java.util.StringTokenizer;

public class CategoryDialog extends Dialog {

    public interface OnClickListener {
        void OnClicked(String category, String currentStock);
    }

    private CategoryDialog.OnClickListener onClickListener;

    public CategoryDialog(@NonNull Context context, OnClickListener onClickListener) {
        super(context);
        setContentView(R.layout.dialog_category);
        this.onClickListener = onClickListener;

        Spinner goodstype = findViewById(R.id.goodsType);
        Spinner currentstock = findViewById(R.id.currentStock);
        TextView tv = findViewById(R.id.selected_category);
        Button finish = findViewById(R.id.finish);

        ArrayAdapter<CharSequence> sd = ArrayAdapter.createFromResource(getContext(), R.array.GoodsType, android.R.layout.simple_spinner_item);
        sd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goodstype.setAdapter(sd);

        goodstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView1, View view, int position1, long id) {
                tv.setText(adapterView1.getItemAtPosition(position1).toString());
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> sgg = ArrayAdapter.createFromResource(getContext(), R.array.CurrentStock, android.R.layout.simple_spinner_item);
        sgg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currentstock.setAdapter(sgg);

        currentstock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String str = tv.getText().toString();
                StringTokenizer st = new StringTokenizer(str, ",");
                tv.setText(st.nextToken() + ", " + adapterView.getItemAtPosition(position).toString());
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.OnClicked(goodstype.getSelectedItem().toString(), currentstock.getSelectedItem().toString());
                dismiss();
            }
        });

    }
}
