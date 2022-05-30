package com.example.mny.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.Market;
import com.example.mny.R;

import java.util.ArrayList;

public class PickMarketAdapter extends RecyclerView.Adapter<PickMarketAdapter.MarketsHolder> {

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;

    private ArrayList<Market> marketList;

    public PickMarketAdapter(ArrayList<Market> marketList, OnListItemSelectedInterface listener) {
        this.marketList = marketList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MarketsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_marketlist, parent, false);

        return new MarketsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketsHolder holder, int position) {
        holder.onBind(marketList.get(position).getMarketname(), marketList.get(position).getAddress_detail(), marketList.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MarketsHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address_detail;
        TextView number;

        public MarketsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address_detail = itemView.findViewById(R.id.address_detail);
            number = itemView.findViewById(R.id.number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });
        }

        void onBind(String name, String address_detail, String number) {
            this.name.setText("가게 이름 : " + name);
            this.address_detail.setText("상세 주소 : " + address_detail);
            this.number.setText("전화 번호 : " + number);
        }

    }
}



