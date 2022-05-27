package com.example.mny;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mny.View.LoginActivity;

public class NoticeDialog extends Dialog {

    public NoticeDialog(@NonNull Context context, String title, String content) {
        super(context);
        setContentView(R.layout.dialog_notice);

        Button finish = findViewById(R.id.finish);
        TextView text = findViewById(R.id.text);

        text.setText(content);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (title) {
                    case "정지!":
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                    case "확인":
                        dismiss();
                }
            }
        });
    }
}
