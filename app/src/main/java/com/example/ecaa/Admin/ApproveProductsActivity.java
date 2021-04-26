package com.example.ecaa.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecaa.Model.Products;
import com.example.ecaa.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewHolder.ProductViewHolder;

public class ApproveProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference unapprovedProductsRef;
    RecyclerView.LayoutManager layoutManager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_products);

        unapprovedProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView=findViewById(R.id.admin_products_checklist);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(unapprovedProductsRef.orderByChild("status").equalTo("unapproved"),Products.class).build() ;
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter= new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull Products products)
            {
                /*not sure about this model thingy.If not initialised to null, giving error, check during integration and execution for potential error*/
                final Products model=products;
                holder.txtProductName.setText(model.getP_name());
                //holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        final String productID= model.getP_id();
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes","No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApproveProductsActivity.this);
                        builder.setTitle("Do you want to approve this product ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which==0)
                                {
                                    ChangeProductStatus (productID);
                                }
                                else if (which==1)
                                {

                                }

                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder= new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter((adapter));
        adapter.startListening();
    }

    private void ChangeProductStatus(String productID)
    {
        unapprovedProductsRef.child(productID).child("status").setValue("approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(ApproveProductsActivity.this, "Item has been approved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}