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

import com.example.mny.Model.Customer;
import com.example.mny.Model.Market;
import com.example.mny.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserHolder> {

    private ArrayList<Customer> cList;
    private ArrayList<Market> mList;
    private String type;
    private Context context;

    public UserListAdapter(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    public void setcList(ArrayList<Customer> cList) {
        this.cList = cList;
    }
    public void setmList(ArrayList<Market> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_ulist, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        if(type.equals("Customer")) holder.onBind(cList.get(position).getNickname(), cList.get(position).getEmail());
        else if(type.equals("Market")) holder.onBind(mList.get(position).getMarketname(), mList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        if(type.equals("Customer")) return cList.size();
        else if(type.equals("Market")) return mList.size();
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void gotoManager(int position) {
        Intent intent;
        if(type.equals("Customer")) {
            intent = new Intent(context, ManageUserActivity.class);
            intent.putExtra("user", cList.get(position));
            intent.putExtra("type", "Customer");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else if(type.equals("Market")) {
            intent = new Intent(context, ManageUserActivity.class);
            intent.putExtra("user", mList.get(position));
            intent.putExtra("type", "Market");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    class UserHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        Button manage;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            manage = itemView.findViewById(R.id.manage);

            manage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoManager(getAdapterPosition());
                }
            });
        }

        void onBind(String names, String emails) {
            name.setText(names);
            email.setText(emails);
        }
    }
}



