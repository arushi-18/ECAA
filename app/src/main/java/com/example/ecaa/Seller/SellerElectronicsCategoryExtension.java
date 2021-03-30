package com.example.ecaa.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.R;

public class SellerElectronicsCategoryExtension extends AppCompatActivity
{
    private ImageView imgAc,imgFridge,imgLaptop,imgMobile,imgTablet,imgTv;
    private String CategoryName,email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_electronics_category_extension);

        imgAc = (ImageView) findViewById(R.id.product_ac);
        imgFridge = (ImageView) findViewById(R.id.product_fridge);
        imgLaptop = (ImageView) findViewById(R.id.product_laptop);
        imgMobile = (ImageView) findViewById(R.id.product_mobile);
        imgTablet = (ImageView) findViewById(R.id.product_tablet);
        imgTv = (ImageView) findViewById(R.id.product_tv);
        CategoryName=getIntent().getExtras().get("Category").toString();
        email=getIntent().getExtras().get("seller_email").toString();

        imgAc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerElectronicsCategoryExtension.this, SellerAddProductDetails.class);
                i.putExtra("SubCategory","AC");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgFridge.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerElectronicsCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Fridge");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgLaptop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerElectronicsCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Laptop");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgMobile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerElectronicsCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Mobile");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgTablet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerElectronicsCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","Tablet");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        imgTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerElectronicsCategoryExtension.this,SellerAddProductDetails.class);
                i.putExtra("SubCategory","TV");
                i.putExtra("Category",CategoryName);
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });
    }
}