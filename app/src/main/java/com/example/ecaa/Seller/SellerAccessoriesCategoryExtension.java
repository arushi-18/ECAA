package com.example.ecaa.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.R;

public class SellerAccessoriesCategoryExtension extends AppCompatActivity
{
    private ImageView imgHat,imgSunglasses,imgWatch;
    private String CategoryName,email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_accessories_category_extension);

        imgHat = (ImageView) findViewById(R.id.product_hat);
        imgSunglasses = (ImageView) findViewById(R.id.product_sunglasses);
        imgWatch = (ImageView) findViewById(R.id.product_watch);
        CategoryName=getIntent().getExtras().get("Category").toString();
        email=getIntent().getExtras().get("seller_email").toString();

        imgHat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerAccessoriesCategoryExtension.this, SellerAddProductDetails.class);
                i.putExtra("SubCategory","Hat");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgSunglasses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerAccessoriesCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Sunglasses");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgWatch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerAccessoriesCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Watch");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });
    }
}