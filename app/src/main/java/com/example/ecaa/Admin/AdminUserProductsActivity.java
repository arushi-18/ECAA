package com.example.ecaa.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecaa.Model.Cart;
import com.example.ecaa.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ViewHolder.cart_view_holder;

public class AdminUserProductsActivity extends AppCompatActivity
{   private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);


        userID=getIntent().getStringExtra("uid");

        productsList=findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        cartListRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, cart_view_holder> adapter=new FirebaseRecyclerAdapter<Cart, cart_view_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cart_view_holder holder, int i, @NonNull Cart model)
            {   holder.txtProductQuantity.setText("Quantity="+model.getQty());
                holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductName.setText(model.getP_name());

            }

            @NonNull
            @Override
            public cart_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
               View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
               cart_view_holder holder=new cart_view_holder(view);
               return holder;
            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();

    }
}