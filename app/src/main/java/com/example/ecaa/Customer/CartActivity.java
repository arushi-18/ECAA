package com.example.ecaa.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecaa.Model.Cart;
import com.example.ecaa.Model.Products;
import com.example.ecaa.Prevalent.Prevalent;
//import com.example.ecaa.ViewHolder.cart_view_holder;
import com.example.ecaa.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import ViewHolder.cart_view_holder;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount,txtMsg1,priceHolder,cartText;
    private long qty;
    private int TotalPrice=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn=(Button) findViewById(R.id.next_btn);
        txtTotalAmount=(TextView) findViewById(R.id.total_price);
        priceHolder=(TextView) findViewById(R.id.total_price_placeholder);
        cartText=(TextView) findViewById(R.id.cart_msg);
        txtMsg1=(TextView) findViewById(R.id.msg1);


        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                txtTotalAmount.setText(String.valueOf(TotalPrice));
                Intent intent=new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(TotalPrice));
                startActivity(intent);
                finish();

            }
        });

    }
    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderStatus();



        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getEmail()).child("Products"), Cart.class).build();
        FirebaseRecyclerAdapter<Cart,cart_view_holder> adapter=new FirebaseRecyclerAdapter<Cart, cart_view_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cart_view_holder holder, int position, @NonNull final Cart model) {
                final Cart cartModel=model;
                holder.txtProductQuantity.setText("Quantity="+cartModel.getQty());
                holder.txtProductPrice.setText(cartModel.getPrice());
                holder.txtProductName.setText(cartModel.getP_name());

                int oneTypeProductTPrice=0;

                if (cartModel.getPrice() != null && cartModel.getQty() != null && !(cartModel.getPrice().equalsIgnoreCase("null")) && !(cartModel.getQty().equalsIgnoreCase("null")) && !cartModel.getPrice().isEmpty() && !cartModel.getQty().isEmpty()) {
                    oneTypeProductTPrice=((Integer.parseInt(cartModel.getPrice())))*Integer.parseInt(cartModel.getQty());
                }
                TotalPrice=TotalPrice+oneTypeProductTPrice;
                txtTotalAmount.setText(String.valueOf(TotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"

                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
                        productsRef.child(cartModel.getP_id()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    Products products=snapshot.getValue(Products.class);
                                    qty = Long.valueOf(products.getQty());

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                if(which==0)
                                {
                                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(cartModel.getP_id());
                                    HashMap<String,Object> productMap = new HashMap<>();
                                    final long nqty=qty+Long.parseLong(cartModel.getQty());
                                    productMap.put("qty",  String.valueOf(nqty));

                                    productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                System.out.println(qty);
                                                System.out.println(nqty);

                                            }else{
                                                System.out.println("************* Error: couldn't update quantity *************");
                                            }
                                        }
                                    });

                                    Intent intent;
                                    intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("p_id",cartModel.getP_id());
                                    startActivity(intent);

                                }
                                if(which==1)
                                {


                                    final long nqty= qty +Long.parseLong(cartModel.getQty());
                                    HashMap <String,Object> productMap = new HashMap<>();

                                    productMap.put("qty",  String.valueOf(nqty));

                                    productsRef.child(cartModel.getP_id()).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                System.out.println(qty);
                                                System.out.println(nqty);
                                                cartListRef.child("User View")
                                                        .child(Prevalent.currentOnlineUser.getEmail())
                                                        .child("Products")
                                                        .child(cartModel.getP_id())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(CartActivity.this,"Item removed successfully",Toast.LENGTH_SHORT).show();
                                                                    Intent intent=new Intent(CartActivity.this, HomeActivityCustomer.class);
                                                                    startActivity(intent);

                                                                }

                                                            }
                                                        });


                                            }else{
                                                System.out.println("************* Error: couldn't update quantity *************");
                                            }
                                        }
                                    });


                                }

                            }
                        });
                        builder.show();


                    }
                });
            }

            @NonNull
            @Override
            public cart_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                cart_view_holder holder=new ViewHolder.cart_view_holder(view);
                return holder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void CheckOrderStatus()
    {
        DatabaseReference ordersRef;
        ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getEmail());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {   if(snapshot.exists())
            {
                String shippingState=snapshot.child("status").getValue().toString();
                String userName=snapshot.child("name").getValue().toString();
                if(shippingState.equals("Shipped"))
                {
                    txtTotalAmount.setText("Dear" + userName+"\n order is shipped successfully");
                    recyclerView.setVisibility(View.GONE);
                    txtMsg1.setVisibility(View.VISIBLE);
                    txtMsg1.setText("Congratulations, your final order has been shipped successfully");
                    NextProcessBtn.setVisibility(View.GONE);

                    Toast.makeText(CartActivity.this,"you can purchase more products once you receive your first final order",Toast.LENGTH_SHORT).show();
                }
                else if(shippingState.equals("not shipped"))
                {
                    //txtTotalAmount.setText("Shipping State=Not Shipped");
                    txtTotalAmount.setVisibility(View.GONE);
                    cartText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    txtMsg1.setVisibility(View.VISIBLE);
                    priceHolder.setVisibility(View.GONE);
                    NextProcessBtn.setVisibility(View.GONE);

                    Toast.makeText(CartActivity.this,"you can purchase more products once you receive your first final order",Toast.LENGTH_SHORT).show();
                }

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}