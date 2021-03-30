package com.example.ecaa.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecaa.LoginActivity;
import com.example.ecaa.MainActivity;
import com.example.ecaa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

//import com.register.R;

public class seller_register extends AppCompatActivity {
    private EditText NameInput,PhoneNumberInput,PasswordInput,EmailInput;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        Button createSellerAccountButton = findViewById(R.id.seller_register_btn);
        NameInput=findViewById(R.id.seller_name_input);
        EmailInput=findViewById(R.id.seller_email_input);
        PasswordInput=findViewById(R.id.seller_password_input);
        PhoneNumberInput= findViewById(R.id.seller_phone_number_input);
        loadingBar=new ProgressDialog(this);

        createSellerAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                CreateSellerAccount();
            }
        });
    }

    private void CreateSellerAccount()
    {
        String email=EmailInput.getText().toString();
        String name=NameInput.getText().toString();
        String password=PasswordInput.getText().toString();
        String phone=PhoneNumberInput.getText().toString();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String phonePattern = "^[+]?[0-9]{10,13}$";

        if(TextUtils.isEmpty(email) || !(email.matches(emailPattern)))
        {
            Toast.makeText(this,"Enter Valid Email",Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(phone) || !(phone.matches(phonePattern)))
        {
            Toast.makeText(this,"Enter Valid Phone Number",Toast.LENGTH_SHORT).show();

        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we check credentials!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            String CleanEmail= email.replaceAll("\\.",",");
            byte[] encodePassword = Base64.encode(password.getBytes(), Base64.DEFAULT);
            validateSellerDetails(CleanEmail,name, Arrays.toString(encodePassword),phone);

        }
    }

    private void validateSellerDetails(final String email, final String name, final String password, final String phone)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("Users").child(email).exists())
                {
                    HashMap<String,Object> userData=new HashMap<>();
                    userData.put("email",email);
                    userData.put("name",name);
                    userData.put("password",password);
                    userData.put("phone",phone);

                    RootRef.child("Sellers").child(email).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            HashMap<String,Object> userTable=new HashMap<>();
                            userTable.put("email",email);
                            userTable.put("password",password);
                            userTable.put("userType","Seller");
                            RootRef.child("Users").child(email).updateChildren(userTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(seller_register.this,"Your account has been created!",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent=new Intent(seller_register.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(seller_register.this,"Network Error!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(seller_register.this,"Network Error!",Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }
                else {
                    Toast.makeText(seller_register.this,"Account already exists!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(seller_register.this,"Please Try again with different email!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(seller_register.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}