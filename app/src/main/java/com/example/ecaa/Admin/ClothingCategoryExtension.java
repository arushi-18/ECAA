package com.example.ecaa.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.R;

public class ClothingCategoryExtension extends AppCompatActivity
{
    private ImageView imgMen,imgWomen,imgKids;
    private String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_category_extension);

        imgMen = (ImageView) findViewById(R.id.product_men);
        imgWomen = (ImageView) findViewById(R.id.product_women);
        imgKids = (ImageView) findViewById(R.id.product_kids);
        CategoryName=getIntent().getExtras().get("category").toString();

        imgMen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ClothingCategoryExtension.this, AdminAddNewProductActivity.class);
                i.putExtra("SubCategory","Men");
                i.putExtra("category",CategoryName);
                startActivity(i);
            }
        });

        imgWomen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ClothingCategoryExtension.this,AdminAddNewProductActivity.class);
                i.putExtra("SubCategory","Women");
                i.putExtra("category",CategoryName);
                startActivity(i);
            }
        });

        imgKids.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ClothingCategoryExtension.this,AdminAddNewProductActivity.class);
                i.putExtra("SubCategory","Kids");
                i.putExtra("category",CategoryName);
                startActivity(i);
            }
        });
    }
}