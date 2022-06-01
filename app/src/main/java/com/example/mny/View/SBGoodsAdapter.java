package com.example.mny.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SBGoodsAdapter extends RecyclerView.Adapter<SBHolder> {
    private Map<String, Object> ginner;
    private List<Map.Entry<String, Object>> inner;

    SBGoodsAdapter(Map<String, Object> ginner) {
        this.ginner = ginner;
        inner = new ArrayList<>(ginner.entrySet());
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
        holder.onBind(inner.get(position).getKey(), inner.get(position).getValue().toString());
    }

    @Override
    public int getItemCount() {
        return ginner.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

class SBHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView count;
    Button remove;

    public SBHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        count = itemView.findViewById(R.id.count);
        remove = itemView.findViewById(R.id.remove);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void onBind(String name, String count) {
        this.name.setText(name);
        this.count.setText(count);
    }
}

