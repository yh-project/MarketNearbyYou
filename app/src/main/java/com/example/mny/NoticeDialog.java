package com.example.mny;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class NoticeDialog extends Dialog {

    public NoticeDialog(@NonNull Context context, String content) {
        super(context);
        setContentView(R.layout.notice_dialog);

        Button finish = findViewById(R.id.finish);
        TextView text = findViewById(R.id.text);

        text.setText(content);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
