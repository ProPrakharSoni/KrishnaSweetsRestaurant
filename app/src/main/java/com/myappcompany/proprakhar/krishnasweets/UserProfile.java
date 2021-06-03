package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity {

    private ImageView profileImg;
    private FirebaseAuth mAuth;
    private Button logoutUser,saveProfile;
    GoogleSignInClient mGoogleSignInClient;
    private EditText name,phone,address,pinCode;
    private Boolean newUser;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profileImg = findViewById(R.id.profilePic);
        logoutUser = findViewById(R.id.logoutUser);
        name=findViewById(R.id.profileName);
        phone=findViewById(R.id.profileNumber);
        address=findViewById(R.id.profileAddress);
        pinCode=findViewById(R.id.profilePinCode);
        saveProfile=findViewById(R.id.saveProfile);
        sharedPreferences=this.getSharedPreferences("com.myappcompany.proprakhar.krishnasweets", Context.MODE_PRIVATE);
        newUser=sharedPreferences.getBoolean("newUser",true);
        SQLiteDatabase myDatabase = this.openOrCreateDatabase("KrishnaSweetsDatabase",MODE_PRIVATE,null);
        myDatabase.execSQL("CREATE table IF NOT EXISTS UserProfile (name Varchar(50) ,phone varchar(20),address varchar(200),pinCode BIGINT)");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ch= check();
                if(newUser&&ch) {
                    myDatabase.execSQL("Insert into UserProfile (name,phone,address,pinCode) values ('" + name.getText().toString() + "','" + phone.getText().toString() + "','" + address.getText().toString() + "'," + Integer.parseInt(pinCode.getText().toString()) + ")");
                    Toast.makeText(UserProfile.this, "Saved", Toast.LENGTH_SHORT).show();
               sharedPreferences.edit().putBoolean("newUser",false).apply();
                }else if(ch){
                    myDatabase.execSQL("DROP table UserProfile");
                    myDatabase.execSQL("CREATE table IF NOT EXISTS UserProfile (name Varchar(50) ,phone varchar(20),address varchar(200),pinCode BIGINT)");
                    myDatabase.execSQL("Insert into UserProfile (name,phone,address,pinCode) values ('" + name.getText().toString() + "','" + phone.getText().toString() + "','" + address.getText().toString() + "'," + Integer.parseInt(pinCode.getText().toString()) + ")");
                    Toast.makeText(UserProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            Cursor c = myDatabase.rawQuery("Select * from UserProfile", null);
            int nameIndex = c.getColumnIndex("name");
            int phoneIndex = c.getColumnIndex("phone");
            int addressIndex = c.getColumnIndex("address");
            int pinCodeIndex = c.getColumnIndex("pinCode");
            c.moveToFirst();
            while (!c.isAfterLast()) {
                name.setText(c.getString(nameIndex));
                phone.setText(c.getString(phoneIndex));
                address.setText(c.getString(addressIndex));
                pinCode.setText(Integer.toString(c.getInt(pinCodeIndex)));
                c.moveToNext();
//                 sharedPreferences.edit().putBoolean(buyItemName+selectedCategory, false).apply();
//                 15:56
//                 mCartItem.add(cartItem);
            }
        }catch (Exception e){
            Toast.makeText(this, "ccatch", Toast.LENGTH_SHORT).show();
            //newUser=true;
            e.printStackTrace();
        }
      //  Toast.makeText(this, String.valueOf(newUser), Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        try {
            Glide.with(getApplicationContext())
                    .load(mAuth.getCurrentUser().getPhotoUrl())
                    .placeholder(R.color.common_google_signin_btn_text_light_disabled)
                    .override(200, 200) // resizing
                    .centerInside()
                    .into(profileImg);
        }catch(Exception e){
            e.printStackTrace();
        }
        }
        private boolean check() {
        int c=0;
        if(name.getText().toString().equals("")){
            name.setError("Empty");
        c++;
        }
        if(phone.getText().toString().equals("")){
            phone.setError("Empty");
            c++;
        }
        if(address.getText().toString().equals("")){
            address.setError("Empty");
            c++;
        } if(pinCode.getText().toString().equals("")){
            pinCode.setError("Empty");
                c++;
        }
        if(c==0) {
            return true;
        }else{
            return false;
        }
    }

    private void signOut() {
        // Firebase sign out
        try {
            mAuth.signOut();
            // Google sign out
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finishAffinity();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            finish();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}