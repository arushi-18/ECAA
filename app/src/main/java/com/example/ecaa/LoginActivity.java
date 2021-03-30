package com.example.ecaa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.Admin.HomeActivityAdmin;
import com.example.ecaa.Customer.HomeActivityCustomer;
import com.example.ecaa.Model.Users;
import com.example.ecaa.Prevalent.Prevalent;
import com.example.ecaa.Seller.SellerChooseCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPassword,InputEmail;
    private ProgressDialog loadingBar;
    private final String ParentDB="Users";
    private TextView ForgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.login_btn);
        InputEmail=findViewById(R.id.login_email_input);
        InputPassword=findViewById(R.id.login_password_input);
        ForgotPasswordLink=findViewById(R.id.forgot_password_link);
        loadingBar=new ProgressDialog(this);

        ForgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                LoginAccount();
            }
        });
    }
    private void LoginAccount()
    {
        String email=InputEmail.getText().toString();
        String password=InputPassword.getText().toString();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(TextUtils.isEmpty(email) || !(email.matches(emailPattern)))
        {
            Toast.makeText(this,"Enter Valid Email",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();

        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait while we check credentials!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            String CleanEmail= email.replaceAll("\\.",",");
            AllowAccess(CleanEmail,password);
        }
    }
    private void AllowAccess(final String email, final String password)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(ParentDB).child(email).exists())
                {
                    Users userData=snapshot.child(ParentDB).child(email).getValue(Users.class);
                    if(userData.getEmail().equals(email))
                    {   byte[] encodePassword= Base64.encode(password.getBytes(), Base64.DEFAULT);
                        if(Arrays.toString(encodePassword).equals(userData.getPassword()))
                        {
                            Toast.makeText(LoginActivity.this,"Logged in successfully!",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            if(userData.getUserType().equals("Customer"))
                            {
                                Intent intent=new Intent(LoginActivity.this, HomeActivityCustomer.class);
                                Users customerData=snapshot.child("Customers").child(email).getValue(Users.class);
                                Prevalent.currentOnlineUser= customerData;
                                startActivity(intent);
                            }
                            else if(userData.getUserType().equals("Seller"))
                            {
                                Intent intent=new Intent(LoginActivity.this, SellerChooseCategory.class);
                                intent.putExtra("seller_email",email);
                                startActivity(intent);
                            }
                            else if(userData.getUserType().equals("Admin"))
                            {
                                Intent intent=new Intent(LoginActivity.this, HomeActivityAdmin.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Kindly enter correct password!",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"This account does not exist!",Toast.LENGTH_SHORT).show();
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