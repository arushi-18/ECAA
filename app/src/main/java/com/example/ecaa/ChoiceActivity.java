package com.example.ecaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {
    private Button customerBtn,sellerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        customerBtn=(Button)findViewById(R.id.sign_in_btn);
        sellerBtn=(Button)findViewById(R.id.sign_up_btn);

        customerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(ChoiceActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        sellerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(ChoiceActivity.this,seller_register.class);
                startActivity(intent);
            }
        });
    }
}