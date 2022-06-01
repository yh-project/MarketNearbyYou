package com.example.mny;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mny.Controller.CustomerMain;

public class SBDialog extends Dialog {

    public interface SBClickListener {
        void OnClicked(String name);
    }

    private SBClickListener onClickListener;

    TextView tvname;
    TextView tvprice;
    TextView tvcurrentStock;

    public SBDialog(@NonNull Context context, String name, String price, String currentStock, SBClickListener onClickListener) {
        super(context);
        setContentView(R.layout.dialog_sb);

        this.onClickListener = onClickListener;
        tvname = findViewById(R.id.name);
        tvprice = findViewById(R.id.price);
        tvcurrentStock = findViewById(R.id.currentStock);
        Button add = findViewById(R.id.add);

        tvname.setText(name);
        tvprice.setText(price);
        tvcurrentStock.setText(currentStock);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.OnClicked(name);
                dismiss();
            }
        });
    }
}
