package com.example.ecaa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView clothing,footwear,electronics,luggage,accessories,personal_care,grocery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        clothing = findViewById(R.id.product_clothing);
        footwear = findViewById(R.id.product_footwear);
        electronics = findViewById(R.id.product_electronics);
        luggage = findViewById(R.id.product_luggage);
        accessories = findViewById(R.id.product_accessories);
        personal_care = findViewById(R.id.product_personal_care);
        grocery = findViewById(R.id.product_grocery);

        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,ClothingCategoryExtension.class);
                intent.putExtra("category","clothing");
                startActivity(intent);
            }
        });

        footwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","footwear");
                startActivity(intent);
            }
        });
        electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,ElectronicsCategoryExtension.class);
                intent.putExtra("category","electronics");
                startActivity(intent);
            }
        });
        luggage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","luggage");
                startActivity(intent);
            }
        });
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,AccessoriesCategoryExtension.class);
                intent.putExtra("category","accessories");
                startActivity(intent);
            }
        });
        personal_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","personalcare");
                startActivity(intent);
            }
        });
        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","grocery");
                startActivity(intent);
            }
        });



    }
}