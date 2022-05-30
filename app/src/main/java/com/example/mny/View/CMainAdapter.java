package com.example.mny.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.CustomerGoods;
import com.example.mny.NoticeDialog;
import com.example.mny.R;
import com.example.mny.SBDialog;

import java.util.ArrayList;

public class CMainAdapter extends RecyclerView.Adapter<CMainAdapter.GoodsHolder> {

    private ArrayList<CustomerGoods> goodsList;

    public CMainAdapter(ArrayList<CustomerGoods> goodsList) {
        this.goodsList = goodsList;
    }

    @NonNull
    @Override
    public GoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_goodslist, parent, false);

        return new GoodsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsHolder holder, int position) {
        holder.onBind(goodsList.get(position).getName(), goodsList.get(position).getPrice(), goodsList.get(position).getCurrentStock());
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class GoodsHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView currentstock;
        Button addSB;
        Button GR;

        public GoodsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            currentstock = itemView.findViewById(R.id.currentStock);
            addSB = itemView.findViewById(R.id.addSB);
            GR = itemView.findViewById(R.id.GR);

            addSB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            GR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        void onBind(String name, int price, String currentstock) {
            this.name.setText(name);
            this.price.setText(price + "원");
            this.currentstock.setText(currentstock);
        }
    }
}



