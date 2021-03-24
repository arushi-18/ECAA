package com.example.ecaa;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity
{   private ImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,addressEditText;
    private TextView profileChangeTextBtn,closeTextBtn,saveTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageView=(ImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText=(EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText=(EditText)findViewById(R.id.settings_phone_number);
        addressEditText=(EditText)findViewById(R.id.settings_address);
        closeTextBtn=(TextView)findViewById(R.id.close_settings_btn);
        saveTextButton=(TextView)findViewById(R.id.update_account_settings_btn);

        userInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);


    }

    private void userInfoDisplay(final ImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, EditText addressEditText)
    {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    if (snapshot.child("image").exists())
                    {
                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}