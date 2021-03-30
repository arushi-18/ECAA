package com.example.ecaa.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.R;

public class SellerClothingCategoryExtension extends AppCompatActivity
{
    private ImageView imgMen,imgWomen,imgKids;
    private String CategoryName,email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_clothing_category_extension);

        imgMen = (ImageView) findViewById(R.id.product_men);
        imgWomen = (ImageView) findViewById(R.id.product_women);
        imgKids = (ImageView) findViewById(R.id.product_kids);
        CategoryName=getIntent().getExtras().get("Category").toString();
        email=getIntent().getExtras().get("seller_email").toString();

        imgMen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerClothingCategoryExtension.this, SellerAddProductDetails.class);
                i.putExtra("SubCategory","Men");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgWomen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerClothingCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Women");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgKids.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerClothingCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Kids");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });
    }
}