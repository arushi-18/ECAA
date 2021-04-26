package com.example.ecaa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.Customer.HomeActivityCustomer;
import com.example.ecaa.Prevalent.Prevalent;
import com.example.ecaa.Seller.SellerHomePage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class settingsActivity extends AppCompatActivity
{   private ImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,addressEditText;
    private TextView profileChangeTextBtn,closeTextBtn,saveTextButton;
    private Button securityQuestionBtn;
    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker="",ParentDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        profileImageView=(ImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText=(EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText=(EditText)findViewById(R.id.settings_phone_number);
        addressEditText=(EditText)findViewById(R.id.settings_address);
        closeTextBtn=(TextView)findViewById(R.id.close_settings_btn);
        saveTextButton=(TextView)findViewById(R.id.update_account_settings_btn);
        profileChangeTextBtn=(TextView)findViewById(R.id.profile_image_change_btn);
        closeTextBtn=(TextView)findViewById(R.id.close_settings_btn);
        securityQuestionBtn=(Button) findViewById(R.id.settings_add_security);

        ParentDB=getIntent().getExtras().get("userType").toString();

        userInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(settingsActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","settings");
                intent.putExtra("userType",ParentDB);
                startActivity(intent);
            }
        });

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateUserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(settingsActivity.this);
                
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this,"Error in uploading image!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(settingsActivity.this, settingsActivity.class));
            finish();
        }
    }

    private void updateUserInfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(ParentDB);
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("phone",userPhoneEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);
        if(ParentDB.equals("Customers"))
            startActivity(new Intent(settingsActivity.this, HomeActivityCustomer.class));
        else if(ParentDB.equals("Sellers"))
            startActivity(new Intent(settingsActivity.this, SellerHomePage.class));
        Toast.makeText(settingsActivity.this,"Profile updated!",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this,"Mandatory field!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"Mandatory field!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this,"Mandatory field!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(checker.equals("clicked"))
            {
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while we check the credentials!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(imageUri!=null)
        {
            final StorageReference fileRef=storageProfilePictureRef.child(Prevalent.currentOnlineUser.getEmail()+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(ParentDB);
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",fullNameEditText.getText().toString());
                        userMap.put("phone",userPhoneEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(settingsActivity.this, HomeActivityCustomer.class));
                        Toast.makeText(settingsActivity.this,"Profile updated!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(settingsActivity.this,"Error in updating!",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(settingsActivity.this,"Image not uploaded!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void userInfoDisplay(final ImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child(ParentDB).child(Prevalent.currentOnlineUser.getEmail());
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
                        addressEditText.setText(address);

                    }
                    else{
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        if (snapshot.child("address").exists()) {
                            String address = snapshot.child("address").getValue().toString();
                            addressEditText.setText(address);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}