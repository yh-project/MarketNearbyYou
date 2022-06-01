package com.example.mny.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.DeliveryData;
import com.example.mny.R;
import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeliveryReservationAdapter extends RecyclerView.Adapter<DeliveryReservationAdapter.DRHolder> {

    private ArrayList<DeliveryData> timeList;
    private LinkedList<String> pickedList = new LinkedList<>();

    public DeliveryReservationAdapter(ArrayList<DeliveryData> timeList) {
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public DRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_deliveryinfo, parent, false);
        return new DRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DRHolder holder, int position) {
        holder.onBind(timeList.get(position).getTime(), timeList.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public LinkedList<String> getPickedList() { return this.pickedList; }

    class DRHolder extends RecyclerView.ViewHolder {

        TextView when;
        TextView reserved;
        CheckBox isReserved;

        public DRHolder(@NonNull View itemView) {
            super(itemView);
            when = itemView.findViewById(R.id.when);
            reserved = itemView.findViewById(R.id.reserved);
            isReserved = itemView.findViewById(R.id.isreserved);

            isReserved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isReserved.isChecked()) pickedList.add(when.getText().toString());
                    else pickedList.remove(when.getText().toString());
                }
            });

        }
        void onBind(String time, String nickname) {
            when.setText(time);
            if(!nickname.equals("")) {
                reserved.setText("예약 중");
                isReserved.setVisibility(View.INVISIBLE);
            } else {
                reserved.setText("예약 가능");
                isReserved.setVisibility(View.VISIBLE);
            }
        }
    }
}



