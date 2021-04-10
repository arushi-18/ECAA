package com.example.ecaa.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.Prevalent.Prevalent;
import com.example.ecaa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{   private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private Button confirm_order_btn;

    private String totalAmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this,"Total Price= Rs."+totalAmount,Toast.LENGTH_SHORT).show();

        confirm_order_btn=(Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText=(EditText) findViewById(R.id.shipment_name);
        phoneEditText=(EditText) findViewById(R.id.shipment_phone_number);
        addressEditText=(EditText) findViewById(R.id.shipment_address);
        cityEditText=(EditText) findViewById(R.id.shipment_city);

        InfoDisplay(nameEditText,phoneEditText,addressEditText);

        confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Check();
            }
        });
    }

    private void InfoDisplay(final EditText nameEditText, final EditText phoneEditText, final EditText addressEditText) {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Customers").child(Prevalent.currentOnlineUser.getEmail());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String name=snapshot.child("name").getValue().toString();
                    String phone=snapshot.child("phone").getValue().toString();
                    nameEditText.setText(name);
                    phoneEditText.setText(phone);
                    if (snapshot.child("address").exists()) {
                        String address = snapshot.child("address").getValue().toString();
                        addressEditText.setText(address);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Check()
    {   if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this,"Please provide your full name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this,"Please provide your phone number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"Please provide your shipping address",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this,"Please provide your city name",Toast.LENGTH_SHORT).show();
        }
        else
        {   ConfirmOrder();

        }
    }
    private void ConfirmOrder()
    {
        String saveCurrentDate,saveCurrentTime;

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getEmail());

        HashMap<String,Object> ordersMap=new HashMap<>();
        ordersMap.put("email",Prevalent.currentOnlineUser.getEmail());
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("status","not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {   if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getEmail())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {   if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this,"Your final order has been placed successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ConfirmFinalOrderActivity.this, HomeActivityCustomer.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //so that customer can't go back
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                }

            }
        });

    }
}