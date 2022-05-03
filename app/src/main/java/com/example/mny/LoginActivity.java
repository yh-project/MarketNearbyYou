package com.example.mny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.login);
        Button join = findViewById(R.id.join);
        Button addmarket = findViewById(R.id.addMarket);

        login.setOnClickListener(onClickListener);
        join.setOnClickListener(onClickListener);
        addmarket.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.join:
                    startActivity(JoinActivity.class);
                    break;
                case R.id.addMarket:
                    startActivity(AddMarketActivity1.class);
                    break;
                case R.id.login:
                    startActivity(CMainActivity.class);
                    break;
            }
        }
    };

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}