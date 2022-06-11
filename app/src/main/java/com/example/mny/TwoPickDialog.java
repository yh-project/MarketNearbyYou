package com.example.mny;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mny.View.LoginActivity;

public class TwoPickDialog extends Dialog {

    public interface RemoveClickLister {
        void Deliveryremovelistener();
    }

    private TwoPickDialog.RemoveClickLister listener;

    public interface BanClickListener {
        void Threelistener();
        void Foreverlistener();
    }

    private TwoPickDialog.BanClickListener banClickListener;

    public TwoPickDialog(@NonNull Context context, String content, String btn1, String btn2, Class c, RemoveClickLister listener, BanClickListener banClickListener) {
        super(context);
        setContentView(R.layout.dialog_twopick);

        TextView contents = findViewById(R.id.content);
        Button b1 = findViewById(R.id.btn1);
        Button b2 = findViewById(R.id.btn2);
        this.listener = listener;
        this.banClickListener = banClickListener;

        contents.setText(content);
        b1.setText(btn1);
        b2.setText(btn2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (btn1) {
                    case "종료":
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                    case "확인":
                        Intent intent = new Intent(getContext(), c);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        break;
                    case "삭제":
                        listener.Deliveryremovelistener();
                        dismiss();
                        break;
                    case "3일 정지":
                        banClickListener.Threelistener();
                        dismiss();
                        break;
                    case "영구 정지":
                        banClickListener.Foreverlistener();
                        dismiss();
                        break;
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (btn2) {
                    case "취소":
                        dismiss();
                        break;
                }
            }
        });
    }


}
