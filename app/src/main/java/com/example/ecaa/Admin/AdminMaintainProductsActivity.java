package com.example.ecaa.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecaa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn,deleteProductBtn;
    private EditText name,price,description,qty;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("p_id");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);


        applyChangesBtn = findViewById(R.id.apply_changes_button);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        qty=findViewById(R.id.product_qty_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteProductBtn=findViewById((R.id.delete_product_button));

        displaySpecificProductInfo( );
        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                applyChanges();
            }
        });
        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteProduct();
            }
        });
    }


        private void deleteProduct()
        {
            productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    Intent intent=new Intent (AdminMaintainProductsActivity.this, HomeActivityAdmin.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AdminMaintainProductsActivity.this,"Product Is Deleted Successfully",Toast.LENGTH_SHORT).show();
                }
            });

        }
        private void applyChanges()
        {
            String pName =name.getText().toString();
            String pPrice =price.getText().toString();
            String pDescription =description.getText().toString();
            String pqty=qty.getText().toString();

            if(pName.equals(" "))
            {
                Toast.makeText(this,"Please Mention The Product Name",Toast.LENGTH_SHORT).show();

            }
            else if(pPrice.equals(" "))
            {
                Toast.makeText(this,"Please Mention The Product Price",Toast.LENGTH_SHORT).show();

            }
            else if(pDescription.equals(" "))
            {
                Toast.makeText(this,"Please Mention The Product Description",Toast.LENGTH_SHORT).show();

            }
            else if(pqty.equals(" "))
            {
                Toast.makeText(this,"Please Mention The Product Quantity",Toast.LENGTH_SHORT).show();

            }
            else
            {
                HashMap<String,Object> productMap = new HashMap<>();
                productMap.put("p_id",productID);
                productMap.put("description",pDescription);
                productMap.put("price",pPrice);
                productMap.put("p_name",pName);
                productMap.put("qty",pqty);

                productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                         if(task.isSuccessful())
                         {
                             Toast.makeText(AdminMaintainProductsActivity.this,"Changes Applied Successfully",Toast.LENGTH_SHORT).show();
                             Intent intent=new Intent (AdminMaintainProductsActivity.this,HomeActivityAdmin.class);
                             startActivity(intent);
                             finish();
                         }
                    }
                });
            }
        }

        private void displaySpecificProductInfo( )
        {
            productsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                        if (snapshot.exists())
                        {
                            String pName= snapshot.child("p_name").getValue().toString();
                            String pPrice= snapshot.child("price").getValue().toString();
                            String pDescription= snapshot.child("description").getValue().toString();
                            String pQty= snapshot.child("qty").getValue().toString();
                            String pImage = snapshot.child("image").getValue().toString();

                            name.setText(pName);
                            price.setText(pPrice);
                            description.setText(pDescription);
                            qty.setText(pQty);
                            Picasso.get().load(pImage).into(imageView);
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


}