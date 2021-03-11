package com.example.ecaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AccessoriesCategoryExtension extends AppCompatActivity {
    private  ImageView hat,sunglass,watch;
    private String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_category_extension);

        hat = (ImageView) findViewById(R.id.product_hat);
        sunglass = (ImageView) findViewById(R.id.product_sunglasses);
        watch = (ImageView) findViewById(R.id.product_watch);
        CategoryName=getIntent().getExtras().get("category").toString();

        hat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(AccessoriesCategoryExtension.this,AdminAddNewProductActivity.class);
                i.putExtra("SubCategory","hat");
                i.putExtra("category",CategoryName);
                startActivity(i);
            }
        });

        sunglass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(AccessoriesCategoryExtension.this,AdminAddNewProductActivity.class);
                i.putExtra("SubCategory","sunglasses");
                i.putExtra("category",CategoryName);
                startActivity(i);
            }
        });

        watch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(AccessoriesCategoryExtension.this,AdminAddNewProductActivity.class);
                i.putExtra("SubCategory","watch");
                i.putExtra("category",CategoryName);
                startActivity(i);
            }
        });

    }
}