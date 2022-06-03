package com.example.mny.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Controller.ShoppingBasket;
import com.example.mny.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SBMarketAdapter extends RecyclerView.Adapter<SBMarketAdapter.SBMHolder> {

    private Map<String, Map<String, Object>> sb;
    private List<Map.Entry<String, Map<String, Object>>> sbEntry;
    private int count = 0;
    private int chosenMarket = 0;
    private LinkedList<String> mList = new LinkedList<>();
    private SBGoodsAdapter.ManageListener listener;

    TextView marketName;
    Button choice;
    RecyclerView gList;
    SBGoodsAdapter sbGoodsAdapter;

    public SBMarketAdapter(Map<String, Map<String, Object>> sb, SBGoodsAdapter.ManageListener listener) {
        this.sb = sb;
        sbEntry = new ArrayList<>(sb.entrySet());
        this.listener = listener;
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
        Log.d("과연", ""+sbEntry.size());
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

    public int mSize() { return this.mList.size(); }
    public LinkedList<String> getMList() { return this.mList; }

    public SBGoodsAdapter getAdapter() { return this.sbGoodsAdapter; }

    class SBMHolder extends RecyclerView.ViewHolder  {

        public SBMHolder(@NonNull View itemView) {
            super(itemView);
            marketName = itemView.findViewById(R.id.marketName);
            choice = itemView.findViewById(R.id.choice);
            gList = itemView.findViewById(R.id.gList);


            choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(choice.getText().toString().equals("취소")) {
                        choice.setText("선택");
                        mList.remove(marketName.getText().toString());
                    } else if(choice.getText().toString().equals("선택")){
                        choice.setText("취소");
                        mList.add(marketName.getText().toString());
                    }
                }
            });
        }
        void onBind(String marketNames) {
            marketName.setText(marketNames);
            sbGoodsAdapter = new SBGoodsAdapter(sbEntry.get(count).getValue(), marketName.getText().toString(), listener);
            gList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.VERTICAL, false));
            gList.setAdapter(sbGoodsAdapter);
        }
    }
}



