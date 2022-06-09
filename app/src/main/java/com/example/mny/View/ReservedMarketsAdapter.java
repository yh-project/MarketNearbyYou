package com.example.mny.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservedMarketsAdapter extends RecyclerView.Adapter<ReservedMarketsAdapter.MRHolder> {

    private List<Map.Entry<String, Map<String, Object>>> reservedList;
    private int count = 0;
    private ReservedGoodsAdapter.GoodsListener listener;

    public ReservedMarketsAdapter() {

    }

    public ReservedMarketsAdapter(Map<String, Map<String, Object>> reservedGoodsList, ReservedGoodsAdapter.GoodsListener listener) {
        reservedList = new ArrayList<>(reservedGoodsList.entrySet());
        this.listener = listener;
    }

    @NonNull
    @Override
    public MRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_rmarket, parent, false);
        return new MRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MRHolder holder, int position) {
        holder.onBind(reservedList.get(position).getKey());
        count++;
    }

    @Override
    public int getItemCount() {
        return reservedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MRHolder extends RecyclerView.ViewHolder {

        TextView marketname;
        RecyclerView reservedGoods;
        ReservedGoodsAdapter reservedGoodsAdapter;

        public MRHolder(@NonNull View itemView) {
            super(itemView);
            marketname = itemView.findViewById(R.id.marketname);
            reservedGoods = itemView.findViewById(R.id.reservedGoods);
        }

        void onBind(String name) {
            marketname.setText(name);
            reservedGoodsAdapter = new ReservedGoodsAdapter(marketname.getText().toString(), reservedList.get(count).getValue(), listener);
            reservedGoods.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.VERTICAL, false));
            reservedGoods.setAdapter(reservedGoodsAdapter);
        }
    }
}



