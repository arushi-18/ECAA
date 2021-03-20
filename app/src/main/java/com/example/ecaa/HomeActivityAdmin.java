package com.example.ecaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivityAdmin extends AppCompatActivity {
    private Button AddProductBtn,MaintainProductBtn,LogoutBtn,CheckNewOrdersBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        AddProductBtn=(Button)findViewById(R.id.add_products);
        MaintainProductBtn=(Button)findViewById(R.id.maintain_product);
        LogoutBtn=(Button)findViewById(R.id.logout_btn);
        CheckNewOrdersBtn=(Button)findViewById(R.id.check_new_orders);


        AddProductBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this,AdminCategoryActivity.class);
                startActivity(intent);
            }
        });

        MaintainProductBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this,HomeActivityCustomer.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        CheckNewOrdersBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this,AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this,HomeActivityCustomer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}