package com.example.ecaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecaa.Admin.HomeActivityAdmin;
import com.example.ecaa.Customer.HomeActivityCustomer;
import com.example.ecaa.Model.Users;
import com.example.ecaa.Prevalent.Prevalent;
import com.example.ecaa.Seller.SellerHomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button signInBtn,signUpBtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn=(Button)findViewById(R.id.sign_in_btn);
        signUpBtn=(Button)findViewById(R.id.sign_up_btn);
        loadingBar=new ProgressDialog(this);

        Paper.init(this);

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(MainActivity.this,ChoiceActivity.class);
                startActivity(intent);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        String UserEmailKey=Paper.book().read(Prevalent.UserEmailKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if (UserEmailKey!="" && UserPasswordKey!="")
        {
            if(!TextUtils.isEmpty(UserEmailKey)&& !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserEmailKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait !");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                // String CleanEmail= UserEmailKey.replaceAll("\\.",",");
            }
        }
    }
    private void AllowAccess(final String email, final String password)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Users").child(email).exists())
                {
                    Users userData=snapshot.child("Users").child(email).getValue(Users.class);
                    if(userData.getEmail().equals(email))
                    {   byte[] encodePassword= Base64.encode(password.getBytes(), Base64.DEFAULT);
                        if(Arrays.toString(encodePassword).equals(userData.getPassword()))
                        {
                            Toast.makeText(MainActivity.this,"Already Logged in!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            if(userData.getUserType().equals("Customer"))
                            {
                                Intent intent=new Intent(MainActivity.this, HomeActivityCustomer.class);
                                Users customerData=snapshot.child("Customers").child(email).getValue(Users.class);
                                Prevalent.currentOnlineUser= customerData;
                                startActivity(intent);
                            }
                            else if(userData.getUserType().equals("Seller"))
                            {
                                Intent intent=new Intent(MainActivity.this, SellerHomePage.class);
                                Users sellerData=snapshot.child("Sellers").child(email).getValue(Users.class);
                                Prevalent.currentOnlineUser= sellerData;
                                //intent.putExtra("seller_email",email);
                                startActivity(intent);
                            }
                            else if(userData.getUserType().equals("Admin"))
                            {
                                Intent intent=new Intent(MainActivity.this, HomeActivityAdmin.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Kindly enter correct password!",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"This account does not exist!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    //Toast.makeText(LoginActivity.this,"Kindly create a new account or use a different email id to login.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
