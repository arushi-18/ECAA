package com.example.ecaa.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {
    private EditText InputName,InputPhoneNumber,InputPassword,InputEmail;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button createAccountButton = findViewById(R.id.register_btn);
        InputName=findViewById(R.id.register_username_input);
        InputEmail=findViewById(R.id.register_email_input);
        InputPassword=findViewById(R.id.register_password_input);
        InputPhoneNumber= findViewById(R.id.register_phone_number_input);
        loadingBar=new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });
    }
    private void CreateAccount()
    {
        String email=InputEmail.getText().toString();
        String name=InputName.getText().toString();
        String password=InputPassword.getText().toString();
        String phone=InputPhoneNumber.getText().toString();

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
            validateDetails(CleanEmail,name, Arrays.toString(encodePassword),phone);

        }
    }

    private void validateDetails(final String email, final String name, final String password, final String phone)
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

                    RootRef.child("Customers").child(email).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                HashMap<String,Object> userTable=new HashMap<>();
                                userTable.put("email",email);
                                userTable.put("password",password);
                                userTable.put("userType","Customer");
                                RootRef.child("Users").child(email).updateChildren(userTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this,"Your account has been created!",Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Account already exists!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please Try again with different email!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}