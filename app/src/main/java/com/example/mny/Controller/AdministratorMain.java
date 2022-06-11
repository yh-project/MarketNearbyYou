package com.example.mny.Controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mny.Model.Customer;
import com.example.mny.Model.Market;
import com.example.mny.Model.User;
import com.example.mny.View.SBMarketAdapter;
import com.example.mny.View.UserListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdministratorMain implements Control {

    private ArrayList<Customer> cList = new ArrayList<>();
    private ArrayList<Market> mList = new ArrayList<>();
    private User currentUser;
    private String type;

    private Context context;
    private RecyclerView uList;
    private UserListAdapter userListAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdministratorMain() {

    }

    public AdministratorMain(Context context, String type, RecyclerView userList) {
        this.context = context;
        this.type = type;
        this.uList = userList;
    }

    public void getList() {
        mUser = mAuth.getCurrentUser();
        if(type.equals("Customer")) {
            cList.clear();
            db.collection("Customer").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    Customer customer = queryDocumentSnapshot.toObject(Customer.class);
                                    cList.add(customer);
                                }
                                showList();
                            }
                        }
                    });
        } else if(type.equals("Market")) {
            mList.clear();
            db.collection("Market").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    Market market = queryDocumentSnapshot.toObject(Market.class);
                                    mList.add(market);
                                }
                                showList();
                            }
                        }
                    });
        }
    }

    public void showList() {
        if(type.equals("Customer")) {
            userListAdapter = new UserListAdapter(context, type);
            userListAdapter.setcList(cList);
            uList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            uList.setAdapter(userListAdapter);
        } else if(type.equals("Market")) {
            userListAdapter = new UserListAdapter(context, type);
            userListAdapter.setmList(mList);
            uList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            uList.setAdapter(userListAdapter);
        }

    }

    @Override
    public void changePage(String pageName) {

    }

    @Override
    public void startToast(String msg) {

    }

    @Override
    public void makeNotice(String type, String msg) {

    }
}
