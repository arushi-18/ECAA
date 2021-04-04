package com.example.ecaa.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecaa.MainActivity;
import com.example.ecaa.Model.Products;
import com.example.ecaa.Prevalent.Prevalent;
import com.example.ecaa.R;
import ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class SellerHomePage extends AppCompatActivity
{
    private TextView message;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;

    //private final

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        message=findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.navigation_home: {
                        Intent intentHome = new Intent(SellerHomePage.this, SellerHomePage.class);
                        startActivity(intentHome);
                        return true;
                    }

                    case R.id.navigation_upload: {
                        Toast.makeText(SellerHomePage.this, "Upload", Toast.LENGTH_SHORT).show();
                        Intent intentCat = new Intent(SellerHomePage.this, SellerChooseCategory.class);
                        intentCat.putExtra("seller_email", Prevalent.currentOnlineUser.getEmail());
                        startActivity(intentCat);
                        return true;
                    }

                    case R.id.navigation_logout: {
                        //final FirebaseAuth mAuth;
                        //mAuth=FirebaseAuth.getInstance();
                        //mAuth.signOut();
                        Intent intentMain = new Intent(SellerHomePage.this, MainActivity.class);
                        intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentMain);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_upload, R.id.navigation_logout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = findViewById(R.id.seller_home_products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedProductsRef.orderByChild("seller_id").equalTo(Prevalent.currentOnlineUser.getEmail()), Products.class)
                .build();
        //FirebaseAuth.getInstance().getCurrentUser().getUid()
        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<Products,ItemViewHolder>(options)
        {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view,parent,false);
                ItemViewHolder holder = new ItemViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, final Products model)
            {
                holder.txtProductName.setText(model.getP_name());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price: " + model.getPrice());
                holder.txtProductStatus.setText("Status: "+model.getStatus());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        final String productID = model.getP_id();

                        final CharSequence[] options = new CharSequence[]
                                {
                                        "Yes", "No"
                                };

                        final AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomePage.this);
                        builder.setTitle("Do you want to delete this product?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                if (position == 0) {
                                    deleteProduct(productID);
                                }
                                if (position == 1) {

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void deleteProduct(String productID)
    {
        unverifiedProductsRef.child(productID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        Toast.makeText(SellerHomePage.this,"That item has been deleted successfully",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}