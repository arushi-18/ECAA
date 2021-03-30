package com.example.ecaa.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.R;

public class SellerChooseCategory extends AppCompatActivity
{
    private ImageView imgAccessories,imgClothing,imgElectronics,imgFootwear;
    private ImageView imgGrocery,imgLuggage,imgPersonalCare;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_choose_category);

        //findViewById() method finds the component by the ID given to it
        imgAccessories = (ImageView) findViewById(R.id.product_accessories);
        imgClothing = (ImageView) findViewById(R.id.product_clothing);
        imgElectronics = (ImageView) findViewById(R.id.product_electronics);
        imgFootwear = (ImageView) findViewById(R.id.product_footwear);
        imgGrocery = (ImageView) findViewById(R.id.product_grocery);
        imgLuggage = (ImageView) findViewById(R.id.product_luggage);
        imgPersonalCare = (ImageView) findViewById(R.id.product_personal_care);
        email=getIntent().getExtras().get("seller_email").toString();

        //for clicking on accessories icon
        imgAccessories.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent is used to determine which component to start, provides runtime binding between separate components
                //Two arguments are passed for Intent - first one is the class the Intent object is in and the second one is the destination class
                //startActivity() method starts another activity here
                //putExtra() method adds the value to intent in key-value pairs
                Intent i = new Intent(SellerChooseCategory.this, SellerAccessoriesCategoryExtension.class);
                i.putExtra("Category","Accessories");
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        //for clicking on clothing icon
        imgClothing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerChooseCategory.this, SellerClothingCategoryExtension.class);
                i.putExtra("Category","Clothing");
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        //for clicking on electronics icon
        imgElectronics.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerChooseCategory.this, SellerElectronicsCategoryExtension.class);
                i.putExtra("Category","Electronics");
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        //for clicking on footwear icon
        imgFootwear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerChooseCategory.this, SellerAddProductDetails.class);
                i.putExtra("Category","Footwear");
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        //for clicking on grocery icon
        imgGrocery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerChooseCategory.this,SellerAddProductDetails.class);
                i.putExtra("Category","Grocery");
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        //for clicking on luggage icon
        imgLuggage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerChooseCategory.this,SellerAddProductDetails.class);
                i.putExtra("Category","Luggage");
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });

        //for clicking on personal care icon
        imgPersonalCare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SellerChooseCategory.this,SellerAddProductDetails.class);
                i.putExtra("Category","PersonalCare");
                i.putExtra("seller_email",email);
                startActivity(i);
            }
        });
    }
}