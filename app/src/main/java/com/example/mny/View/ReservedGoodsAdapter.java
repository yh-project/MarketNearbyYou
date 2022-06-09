package com.example.mny.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservedGoodsAdapter extends RecyclerView.Adapter<ReservedGoodsAdapter.MRGHolder> {

    public interface GoodsListener {
        void RemoveListener(String marketName, String name);
    }

    private ReservedGoodsAdapter.GoodsListener listener;

    private List<Map.Entry<String, Object>> goodsforMarket;
    private String marketName;

    public ReservedGoodsAdapter() {

    }

    public ReservedGoodsAdapter(String marketName, Map<String, Object> reservedGoodsList, GoodsListener listener) {
        goodsforMarket = new ArrayList<>(reservedGoodsList.entrySet());
        this.marketName = marketName;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MRGHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_rgoods, parent, false);
        return new MRGHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MRGHolder holder, int position) {
        holder.onBind(goodsforMarket.get(position).getKey(), goodsforMarket.get(position).getValue().toString());
    }

    @Override
    public int getItemCount() {
        return goodsforMarket.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MRGHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView currentStock;
        TextView remove;

        public MRGHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            currentStock = itemView.findViewById(R.id.currentStock);
            remove = itemView.findViewById(R.id.remove);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.RemoveListener(marketName, goodsforMarket.get(getAdapterPosition()).getKey());
                }
            });

        }
        void onBind(String names, String currentStocks) {
            name.setText(names);
            currentStock.setText(currentStocks);
        }
    }
}



