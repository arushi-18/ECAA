package com.example.ecaa.Seller;

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

public class SellerAddProductDetails extends AppCompatActivity
{
    private String categoryName,SubCategoryName,description,price,productName,qty,seller_email;
    private ImageView imgInputProduct;
    private EditText inputProductName,inputProductDescription,inputProductPrice,inputProductQuantity;
    private static final int GalleryPick=1;
    private Uri imageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product_details);

        categoryName=getIntent().getExtras().get("Category").toString();
        seller_email=getIntent().getExtras().get("seller_email").toString();
        if(categoryName.equals("Accessories") || categoryName.equals("Electronics") || categoryName.equals("Clothing"))
            SubCategoryName=getIntent().getExtras().get("SubCategory").toString();
        productImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        //Toast.makeText(this, categoryName,Toast.LENGTH_SHORT).show();

        Button addNewProductBtn = (Button) findViewById(R.id.add_new_product);
        imgInputProduct=(ImageView) findViewById(R.id.upload_product_image);
        inputProductName=(EditText) findViewById(R.id.product_name);
        inputProductDescription=(EditText) findViewById(R.id.product_description);
        inputProductPrice=(EditText) findViewById(R.id.product_price);
        inputProductQuantity= findViewById(R.id.product_qty);
        loadingBar=new ProgressDialog(this);

        imgInputProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        addNewProductBtn.setOnClickListener(new View.OnClickListener()
        {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            imgInputProduct.setImageURI(imageUri);
        }
    }

    private void ValidateProductData()
    {
        description=inputProductDescription.getText().toString();
        price=inputProductPrice.getText().toString();
        productName=inputProductName.getText().toString();
        qty=inputProductQuantity.getText().toString();

        if(imageUri==null)
        {
            Toast.makeText(this,"Product image is mandatory!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this,"Please write product description! ",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(this,"Please write product price! ",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productName))
        {
            Toast.makeText(this,"Please write product name! ",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(qty))
        {
            Toast.makeText(this,"Please write product name! ",Toast.LENGTH_SHORT).show();
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
        productRandomKey=productsRef.child("Products").push().getKey();

        final StorageReference filePath=productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask=filePath.putFile(imageUri);

        //incase of failure
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {   loadingBar.dismiss();
                String message=e.toString();
                Toast.makeText(SellerAddProductDetails.this,"Error" + message,Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                //Toast.makeText(SellerAddProductDetails.this,"Product Image uploaded successfully",Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(SellerAddProductDetails.this,"Product Image Successfully uploaded",Toast.LENGTH_SHORT).show();

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
        productMap.put("seller_id",seller_email);
        productMap.put("status","unapproved");
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("p_name",productName);
        productMap.put("qty",qty);
        if(SubCategoryName!=null)
            productMap.put("sub_category",SubCategoryName);


        productsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
        {   @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {   loadingBar.dismiss();
                    Toast.makeText(SellerAddProductDetails.this, "Product is uploaded successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SellerAddProductDetails.this, SellerChooseCategory.class);
                    startActivity(intent);
                }
                else
                {   loadingBar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(SellerAddProductDetails.this,"Error"+message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){

        Intent intent=new Intent(SellerAddProductDetails.this, SellerChooseCategory.class);
        startActivity(intent);
    }

}
