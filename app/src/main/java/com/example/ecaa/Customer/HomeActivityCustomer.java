package com.example.ecaa.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecaa.Admin.AdminMaintainProductsActivity;
import com.example.ecaa.MainActivity;
import com.example.ecaa.Model.Products;
import com.example.ecaa.Prevalent.Prevalent;
import com.example.ecaa.R;
import com.example.ecaa.SearchActivity;
import com.example.ecaa.settingsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ViewHolder.ProductViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivityCustomer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference ProductsRef;
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type="";
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_customer);

        //type=getIntent().getExtras().get("Admin").toString();

        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            type=getIntent().getExtras().get("Admin").toString();
        }



        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivityCustomer.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView=navigationView.getHeaderView(0);
        TextView userNameTextView=headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView=headerView.findViewById(R.id.user_profile_image);

        if (!type.equals("Admin"))
        {   final String mail=Prevalent.currentOnlineUser.getEmail().replaceAll("\\,",".");
            userNameTextView.setText(mail);
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }

        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("status").equalTo("approved"),Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder>adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getP_name());
                        //holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent;
                                if (type.equals("Admin")) {
                                    intent = new Intent(HomeActivityCustomer.this, AdminMaintainProductsActivity.class);

                                } else {
                                    intent = new Intent(HomeActivityCustomer.this, ProductDetailsActivity.class);
                                }
                                intent.putExtra("p_id", model.getP_id());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder=new ProductViewHolder(view);
                        return holder;

                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity_customer, menu);
        return true;
    }

    @SuppressWarnings("statementWithoutBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.nav_cart){
            if(!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivityCustomer.this, CartActivity.class);
                startActivity(intent);
            }

        }
        else if(id==R.id.nav_categories)
        {

        }
        else if(id==R.id.nav_search)
        {
            if(!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivityCustomer.this, SearchActivity.class);
                startActivity(intent);
            }
        }
        else if(id==R.id.nav_settings)
        {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivityCustomer.this, settingsActivity.class);
                intent.putExtra("userType","Customers");
                startActivity(intent);
            }
        }
        else if(id==R.id.nav_logout)
        {
            if(!type.equals("Admin")) {
                Paper.book().destroy();
                Intent intent = new Intent(HomeActivityCustomer.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        return false;
    }

    /*@Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();

            System.exit(0);
            /*Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
            /*int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }*/
}