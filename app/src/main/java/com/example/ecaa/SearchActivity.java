package com.example.ecaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecaa.Customer.ProductDetailsActivity;
import com.example.ecaa.Model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import ViewHolder.ProductViewHolder;

public class SearchActivity extends AppCompatActivity {

    private String SearchInput="";
    private EditText inputText;
    private RecyclerView searchList;
    RecyclerView.LayoutManager layoutManager;
    int adapterFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inputText=findViewById(R.id.search_product_name);
        Button searchBtn = findViewById(R.id.search_btn);
        searchList=findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SearchInput= inputText.getText().toString();
                if(SearchInput.indexOf(' ')!=-1) {
                    for (int i = 0; i < SearchInput.length(); i++) {
                        char ch = SearchInput.charAt(i);
                        if (ch == ' ')
                            SearchInput = SearchInput.substring(0, i + 1) + Character.toUpperCase(SearchInput.charAt(i + 1)) + SearchInput.substring(i + 2);
                    }
                }
                SearchInput=SearchInput.substring(0,1).toUpperCase()+SearchInput.substring(1);
                if(TextUtils.isEmpty(SearchInput))
                {
                    Toast.makeText(SearchActivity.this,"Enter Search Query!",Toast.LENGTH_SHORT).show();

                }
                else {
                    onStart();
                }

            }
        });



    }


    protected void onStart()
    {
        super.onStart();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");
        final FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("p_name").startAt(SearchInput).endAt(SearchInput+"\uf8ff"),Products.class).build();
        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull Products products)
            {
                final Products model=products;
                if("approved".equals(model.getStatus())) {
                    holder.txtProductName.setText(model.getP_name());
                    //holder.txtProductDescription.setText(model.getDescription());
                    holder.txtProductPrice.setText(model.getPrice());
                    Picasso.get().load(model.getImage()).into(holder.imageView);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("p_id", model.getP_id());
                            startActivity(intent);

                        }
                    });
                //holder.setIsRecyclable(false);
               }
                else
                {
                    holder.itemView.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                    adapterFlag=1;
                }
                
            }


            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
            @Override
            public void onDataChanged() {
                // do your thing
                if (getItemCount() == 0)
                    Toast.makeText(SearchActivity.this, "No results found!", Toast.LENGTH_SHORT).show();
                if (getItemCount() == 1 && adapterFlag==1) {
                    Toast.makeText(SearchActivity.this, "No results found!", Toast.LENGTH_SHORT).show();
                    adapterFlag = 0;
                }
            }

        };

        layoutManager=new LinearLayoutManager(this);
        searchList.setLayoutManager(layoutManager);
        searchList.setAdapter(adapter);
        adapter.startListening();


    }
}





