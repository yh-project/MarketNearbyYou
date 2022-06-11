package com.example.mny.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.DeliveryData;
import com.example.mny.R;

import java.util.ArrayList;

public class ManageDeliveryListAdapter extends RecyclerView.Adapter<ManageDeliveryListAdapter.GoodsHolder> {

    private ArrayList<DeliveryData> deliveryList;
    private Context context;

    public ManageDeliveryListAdapter(ArrayList<DeliveryData> deliveryList, Context context) {
        this.deliveryList = deliveryList;
        this.context = context;
    }

    @NonNull
    @Override
    public GoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_deliverymanage, parent, false);
        return new GoodsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsHolder holder, int position) {
        holder.onBind(deliveryList.get(position));
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void gotoManager(int position) {
        DeliveryData deliveryData = deliveryList.get(position);
        Intent intent = new Intent(context, ManageDeliveryActivity.class);
        intent.putExtra("Delivery", deliveryData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public class GoodsHolder extends RecyclerView.ViewHolder {

        TextView when;
        Button edit;

        public GoodsHolder(@NonNull View itemView) {
            super(itemView);
            when = itemView.findViewById(R.id.when);
            edit = itemView.findViewById(R.id.edit);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoManager(getAdapterPosition());
                }
            });
        }

        void onBind(DeliveryData deliveryData) {
            when.setText(deliveryData.getTime());
            if(deliveryData.getNickname().equals("")) {
                edit.setText("수정 불가");
                edit.setClickable(false);
            }
        }
    }
}



