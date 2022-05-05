package com.example.mny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SBGoodsAdapter extends RecyclerView.Adapter<SBHolder> {
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<String> stocks;

    SBGoodsAdapter(ArrayList<String> names, ArrayList<String> prices, ArrayList<String> stocks) {
        this.names = names;
        this.prices = prices;
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public SBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sbgoods, parent, false);

        return new SBHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SBHolder holder, int position) {
        holder.onBind(names.get(position), prices.get(position), stocks.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

class SBHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView price;
    TextView count;
    Button remove;

    public SBHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        count = itemView.findViewById(R.id.count);
        remove = itemView.findViewById(R.id.remove);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    void onBind(String name, String price, String count) {
        this.name.setText(name);
        this.price.setText(price);
        this.count.setText(count);
    }

}

