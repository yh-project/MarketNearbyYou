package com.example.mny.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SBMarketAdapter extends RecyclerView.Adapter<SBMarketAdapter.SBMHolder> {
    private Map<String, Map<String, Object>> sb;
    private List<Map.Entry<String, Map<String, Object>>> sbEntry;
    private int count;

    public SBMarketAdapter(Map<String, Map<String, Object>> sb) {
        this.sb = sb;
        sbEntry = new ArrayList<>(sb.entrySet());
    }

    @NonNull
    @Override
    public SBMHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sblistmarket, parent, false);
        return new SBMHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SBMHolder holder, int position) {
        holder.onBind(sbEntry.get(position).getKey());
        count++;
    }

    @Override
    public int getItemCount() {
        return sb.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class SBMHolder extends RecyclerView.ViewHolder {
        TextView marketName;
        Button choice;
        RecyclerView gList;
        SBGoodsAdapter sbGoodsAdapter;

        public SBMHolder(@NonNull View itemView) {
            super(itemView);
            marketName = itemView.findViewById(R.id.marketName);
            choice = itemView.findViewById(R.id.choice);
            gList = itemView.findViewById(R.id.gList);
            sbGoodsAdapter = new SBGoodsAdapter(sbEntry.get(count).getValue());
            gList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.VERTICAL, false));
            gList.setAdapter(sbGoodsAdapter);

            choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        void onBind(String marketName) {
            this.marketName.setText(marketName);
        }
    }
}



