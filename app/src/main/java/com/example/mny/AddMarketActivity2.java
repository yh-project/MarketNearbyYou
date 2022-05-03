package com.example.mny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class AddMarketActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmarket2);

        Spinner seedo = findViewById(R.id.seedo);
        TextView tv = findViewById(R.id.text_address);

        ArrayAdapter<CharSequence> sd = ArrayAdapter.createFromResource(this, R.array.seedo, android.R.layout.simple_spinner_item);
        sd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seedo.setAdapter(sd);

        seedo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                tv.setText(adapterView.getItemAtPosition(position).toString());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}