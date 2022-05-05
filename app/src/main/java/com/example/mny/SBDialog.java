package com.example.mny;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SBDialog extends Dialog {

    public SBDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_sb);

        Button add = findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
