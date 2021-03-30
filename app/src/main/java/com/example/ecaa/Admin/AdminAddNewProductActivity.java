package com.example.ecaa.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName,SubCategoryName,Description,Price,Pname,Pqty;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription,InputProductPrice,InputProductQuantity;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName=getIntent().getExtras().get("category").toString();
        if(CategoryName.equals("accessories") || CategoryName.equals("electronics") || CategoryName.equals("clothing"))
            SubCategoryName=getIntent().getExtras().get("SubCategory").toString();
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        //Toast.makeText(this, CategoryName,Toast.LENGTH_SHORT).show();

        Button addNewProductButton = findViewById(R.id.add_new_product);
        InputProductImage= findViewById(R.id.select_product_image);
        InputProductName= findViewById(R.id.product_name);
        InputProductDescription= findViewById(R.id.product_description);
        InputProductPrice= findViewById(R.id.product_price);
        InputProductQuantity= findViewById(R.id.product_qty);
        loadingBar=new ProgressDialog(this);

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });

    }
    private void OpenGallery()
    {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);

        }
    }
    private void ValidateProductData()
    {
        Description=InputProductDescription.getText().toString();
        Price=InputProductPrice.getText().toString();
        Pname=InputProductName.getText().toString();
        Pqty=InputProductQuantity.getText().toString();

        if(ImageUri==null)
        {
            Toast.makeText(this,"Product image is mandatory!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Please write product description! ",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this,"Please write product price! ",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this,"Please write product name! ",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pqty))
        {
            Toast.makeText(this,"Please write product quantity! ",Toast.LENGTH_SHORT).show();
        }
        else
        {   loadingBar.setTitle("Adding Product");
            loadingBar.setMessage("Please wait while we check details!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            StoreProductInformation();
        }
    }
    private void StoreProductInformation()
    {
        /*Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate + saveCurrentTime; //a unique key*/
        //final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        productRandomKey = ProductsRef.child("Products").push().getKey();

        final StorageReference filePath=ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask=filePath.putFile(ImageUri);

        //incase of failure
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message=e.toString();
                Toast.makeText(AdminAddNewProductActivity.this,"Error! Could not upload image.\n" + message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                //Toast.makeText(AdminAddNewProductActivity.this,"Product Image uploaded successfully",Toast.LENGTH_SHORT).show();

                //get image url

                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {   downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"Product Image uploaded successfully!",Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });

            }
        });
    }
    private void SaveProductInfoToDatabase()
    {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("p_id",productRandomKey);
        productMap.put("seller_id","admin");
        productMap.put("status","approved");
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("p_name",Pname);
        productMap.put("qty",Pqty);
        if(SubCategoryName!=null)
            productMap.put("sub_category",SubCategoryName);

        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Product Added Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);
                }
                else {
                    loadingBar.dismiss();
                    String emessage=task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this,"Error:"+emessage,Toast.LENGTH_SHORT).show();
                }
            }
        });








    }






}