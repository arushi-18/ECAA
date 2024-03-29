package com.example.ecaa.Customer;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.ecaa.Model.Products;
import com.example.ecaa.Prevalent.Prevalent;
import com.example.ecaa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String state="Normal";


    private long qty;
    private int num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        final String productID = getIntent().getStringExtra("p_id");

        addToCartBtn = (Button) findViewById(R.id.add_product_to_cart_btn);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);




        getProductDetails(productID);



        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(numberButton.getNumber());

                if( (state.equals("Order Placed") || state.equals("Order shipped")))
                {
                    Toast.makeText(ProductDetailsActivity.this, "You can purchase more products once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else if(qty > num)
                {   Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_LONG).show();
                    updateQuantity(productID);
                    addingToCartList(productID);
                }
                else if(qty<num)
                {
                    System.out.println(qty + " **************************************************** " + num);
                    Toast.makeText(ProductDetailsActivity.this, "Quantity out of stock", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ProductDetailsActivity.this, "Product does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addingToCartList(final String productID)
    {   String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd/mm/yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object>  cartMap=new HashMap<>();
        cartMap.put("p_id",productID);
        cartMap.put("p_name",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("qty", numberButton.getNumber());
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getEmail()).child("Products")
                .child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getEmail()).child("Products")
                        .child(productID).updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ProductDetailsActivity.this,"Added to cart list",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ProductDetailsActivity.this, HomeActivityCustomer.class);
                                    startActivity(intent);
                                }

                            }
                        });
                }
            }
        });



    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Products products=snapshot.getValue(Products.class);
                    productName.setText(products.getP_name());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);

                    qty = Integer.valueOf(products.getQty());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getEmail());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {   if(snapshot.exists()){
                String shippingState=snapshot.child("status").getValue().toString();

                if(shippingState.equals("Shipped"))
                {
                    state="Order Shipped";
                }
                else if(shippingState.equals("not shipped"))
                {
                    state="Order Placed";
                }

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateQuantity(String productID)
    {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("qty",  String.valueOf(qty - num));
        productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    System.out.println(qty);
                    System.out.println(num);
                    System.out.println(qty-num);
                    System.out.println("Quantity updated");

                }else{
                    System.out.println("************* Error: couldn't update quantity *************");
                }
            }
        });
    }

}