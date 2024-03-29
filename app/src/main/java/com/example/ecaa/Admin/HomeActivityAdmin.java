package com.example.ecaa.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecaa.Customer.HomeActivityCustomer;
import com.example.ecaa.MainActivity;
import com.example.ecaa.R;

import io.paperdb.Paper;

public class HomeActivityAdmin extends AppCompatActivity {
    private Button AddProductBtn,MaintainProductBtn,LogoutBtn,CheckNewOrdersBtn,VerifyProductsBtn;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        AddProductBtn=(Button)findViewById(R.id.add_products);
        MaintainProductBtn=(Button)findViewById(R.id.maintain_product);
        LogoutBtn=(Button)findViewById(R.id.logout_btn);
        CheckNewOrdersBtn=(Button)findViewById(R.id.check_new_orders);
        VerifyProductsBtn=(Button)findViewById(R.id.verify_products_btn);


        AddProductBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });

        VerifyProductsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this, ApproveProductsActivity.class);
                startActivity(intent);
            }
        });

        MaintainProductBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this, HomeActivityCustomer.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        CheckNewOrdersBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(HomeActivityAdmin.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Paper.book().destroy();
                Intent intent=new Intent(HomeActivityAdmin.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}