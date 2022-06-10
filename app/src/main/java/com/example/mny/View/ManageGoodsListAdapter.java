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
import com.example.mny.Model.Goods;
import com.example.mny.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageGoodsListAdapter extends RecyclerView.Adapter<ManageGoodsListAdapter.GoodsHolder> {

    private ArrayList<Goods> goodsList;
    private Context context;

    public ManageGoodsListAdapter(ArrayList<Goods> goodsList, Context context) {
        this.goodsList = goodsList;
        this.context = context;
    }

    @NonNull
    @Override
    public GoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_goodsmanage, parent, false);
        return new GoodsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsHolder holder, int position) {
        holder.onBind(goodsList.get(position));
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void gotoManager(int position) {
        Goods goods = goodsList.get(position);
        Intent intent = new Intent(context, ManageGoodsActivity.class);
        intent.putExtra("Goods", goods);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public class GoodsHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView currentStock;
        Button edit;

        public GoodsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            currentStock = itemView.findViewById(R.id.currentStock);
            edit = itemView.findViewById(R.id.edit);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoManager(getAdapterPosition());
                }
            });
        }

        void onBind(Goods goods) {
            name.setText(goods.getName());
            price.setText(Integer.toString(goods.getPrice()));
            currentStock.setText(goods.getCurrentStock());
        }
    }
}



