package com.example.ecaa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecaa.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check="";
    private TextView pagetitle,titlequestions;
    private EditText findEmail,question1,question2;
    private Button verifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=getIntent().getStringExtra("check");
        pagetitle=findViewById(R.id.page_title);
        titlequestions=findViewById(R.id.title_questions);
        findEmail=findViewById(R.id.find_email);
        question1=findViewById(R.id.question_1);
        question2=findViewById(R.id.question_2);
        verifyButton=findViewById(R.id.verify);




    }

    @Override
    protected void onStart() {
        super.onStart();
        findEmail.setVisibility(View.GONE);
        if(check.equals("settings"))
        {
            pagetitle.setText("Set Questions");
            titlequestions.setText("Please set answers for the following questions");
            verifyButton.setText("Set");
            displayPreviousAnswers();


            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();

                }
                });



        }
        else if(check.equals("login"))
        {
            findEmail.setVisibility(View.VISIBLE);
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }

    }
    private  void setAnswers()
    {
        String answer1= question1.getText().toString().toLowerCase();
        String answer2= question2.getText().toString().toLowerCase();

        if(question1.equals("") || question2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this,"Please answer both questions.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Customers")
                    .child(Prevalent.currentOnlineUser.getEmail());
            HashMap<String,Object> userdataMap=new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "you have answered the security questions successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPasswordActivity.this, settingsActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private void displayPreviousAnswers() {
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference()
                .child("Customers")
                .child(Prevalent.currentOnlineUser.getEmail());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String answer1 = snapshot.child("answer1").getValue().toString();
                    String answer2 = snapshot.child("answer2").getValue().toString();
                    question1.setText(answer1);
                    question2.setText(answer2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifyUser()
    {
        String UserEmail= findEmail.getText().toString().toLowerCase();

        final String ans1= question1.getText().toString().toLowerCase();
        final String ans2= question2.getText().toString().toLowerCase();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(TextUtils.isEmpty(UserEmail) || !(UserEmail.matches(emailPattern)))
        {
            Toast.makeText(this,"Enter Valid Email",Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(ans1))
        {
            Toast.makeText(this,"Enter Answer One",Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(ans2))
        {
            Toast.makeText(this,"Enter Answer Two",Toast.LENGTH_SHORT).show();

        }
        else{
            final String cleanEmail=UserEmail.replaceAll("\\.",",");
            final DatabaseReference ref= FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(cleanEmail);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String DbType="";
                        String type=snapshot.child("userType").getValue().toString();
                        if(type.equals("Customer"))
                            DbType="Customers";
                        else if(type.equals("Seller"))
                            DbType="Sellers";
                        final DatabaseReference DataRef= FirebaseDatabase.getInstance()
                                .getReference()
                                .child(DbType)
                                .child(cleanEmail);
                        DataRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("Security Questions")) {
                                    String answer1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                                    String answer2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                                    if (!(ans1.equals(answer1))) {
                                        Toast.makeText(ResetPasswordActivity.this, "Answer One is not correct! ", Toast.LENGTH_SHORT).show();
                                    }
                                    if (!(ans2.equals(answer2))) {
                                        Toast.makeText(ResetPasswordActivity.this, "Answer Two is not correct! ", Toast.LENGTH_SHORT).show();
                                    }
                                    if (ans1.equals(answer1) && ans2.equals(answer2)) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                        builder.setTitle("Set New Password");

                                        final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                        newPassword.setHint("Enter new Password");
                                        newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        builder.setView(newPassword);
                                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (!(newPassword.getText().toString().equals(""))) {
                                                    String newPass=newPassword.getText().toString();
                                                    byte[] encodePassword = Base64.encode(newPass.getBytes(), Base64.DEFAULT);
                                                    newPass= Arrays.toString(encodePassword);

                                                    final String finalNewPass = newPass;
                                                    ref.child("password").setValue(newPass)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        DataRef.child("password").setValue(finalNewPass)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(ResetPasswordActivity.this, "New Password has been set successfully!", Toast.LENGTH_SHORT).show();
                                                                                        Intent intent=new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });

                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder.show();
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "This user account does not exists!", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}