package com.example.mny.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Controller.Control;
import com.example.mny.Model.Goods;
import com.example.mny.NoticeDialog;
import com.example.mny.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SBGoodsAdapter extends RecyclerView.Adapter<SBGoodsAdapter.SBHolder> implements Control {

    public interface ManageListener {
        void IncreaseListener(String marketName, int position);
        void ReduceListener(String marketName, int position);
        void RemoveListener(String marketName, int position);
    }

    private ManageListener listener;

    private PickMarketAdapter.OnListItemSelectedInterface mListener;
    private Map<String, Object> ginner;
    private List<Map.Entry<String, Object>> inner;
    private String marketName;

    TextView name;
    TextView count;
    Button remove;
    Button increase;
    Button reduce;
    Context context;

    SBGoodsAdapter(Map<String, Object> ginner, String marketName, ManageListener listener) {
        this.ginner = ginner;
        inner = new ArrayList<>(ginner.entrySet());
        this.marketName = marketName;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
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

    @Override
    public void changePage(String pageName) { }
    @Override
    public void startToast(String msg) { }
    @Override
    public void makeNotice(String type, String msg) {
        NoticeDialog nd = new NoticeDialog(context, type, msg);
        nd.show();
    }

    class SBHolder extends RecyclerView.ViewHolder {

        public SBHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            count = itemView.findViewById(R.id.count);
            remove = itemView.findViewById(R.id.remove);
            increase = itemView.findViewById(R.id.increase);
            reduce = itemView.findViewById(R.id.reduce);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.RemoveListener(marketName, getAdapterPosition());
                }
            });
            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.IncreaseListener(marketName, getAdapterPosition());
                }
            });
            reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ReduceListener(marketName, getAdapterPosition());
                }
            });
        }

        void onBind(String names, String counts) {
            name.setText(names);
            count.setText(counts);
        }
    }
}



