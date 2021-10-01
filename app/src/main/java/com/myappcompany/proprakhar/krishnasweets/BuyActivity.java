package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class BuyActivity extends AppCompatActivity {
   // glide faster than picasso
    private ImageView buyItemImage;
   // private ImageView cartImage,profile;
    private RadioGroup radioGroup;
    private FirebaseAuth mAuth;
    private TextView itemName,price,buyQuantity;
    private Button cart,buy,inc,dec;
    private RadioButton category1Radio,category2Radio;
    private String selectedCategory,selectedPrice,url,buyItemName;
    private SharedPreferences sharedPreferences;
    private Boolean isFirstTimeAddToCart;
    private int qty=1;
    private DrawerLayout drawerLayout;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
//        cartImage =findViewById(R.id.cart);
//        profile=findViewById(R.id.profile);
        drawerLayout= findViewById(R.id.drawerlayout);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//  //      cartImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),CartActivity.class));
//            }
//        });
//        Picasso.get()
//                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSV7VoOgaHUhoeKNRFDpyL1D__B72rfkIuFrA&usqp=CAU")
//                //.placeholder(R.mipmap.ic_launcher)
//                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
//                .centerInside()
//                .fit()
//                .transform(new CropCircleTransformation())
//                .into(cartImage);
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // intent.putExtra("giveItem","");
//                //startActivity(intent);
//                startActivity(new Intent(getApplicationContext(),UserProfile.class));
//            }
//        });
//        Picasso.get()
//                .load(mAuth.getCurrentUser().getPhotoUrl())
//                //.placeholder(R.mipmap.ic_launcher)
//                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
//                .centerInside()
//                .fit()
//                .transform(new CropCircleTransformation())
//                .into(profile);
        sharedPreferences=this.getSharedPreferences("com.myappcompany.proprakhar.krishnasweets", Context.MODE_PRIVATE);
        buyItemImage=findViewById(R.id.buyItemImage);
        buyQuantity=findViewById(R.id.buyQuantity);
        inc=findViewById(R.id.itemInc);
        dec=findViewById(R.id.itemDec);
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty++;
                buyQuantity.setText(Integer.toString(qty));
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qty!=1) {
                    qty--;
                    buyQuantity.setText(Integer.toString(qty));
                }
            }
        });
        radioGroup=findViewById(R.id.radioGroup);
        itemName=findViewById(R.id.buyItemName);
        price=findViewById(R.id.buyItemPrice);
        category1Radio=findViewById(R.id.cashOnDelivery);
        category2Radio=findViewById(R.id.radioCategory2);
        buy=findViewById(R.id.BuyNowButton);
        cart=findViewById(R.id.AddToCartButton);
        Intent i = getIntent();
        Intent buyIntent=new Intent(getApplicationContext(),PaymentActivity.class);
        Intent intent=new Intent(getApplicationContext(),CartActivity.class);
         url =i.getStringExtra("url");
        String category1Name=i.getStringExtra("category1Name");
        String category1Price=i.getStringExtra("category1Price");
        String category2Name=i.getStringExtra("category2Name");
        String category2Price=i.getStringExtra("category2Price");
        buyItemName=i.getStringExtra("itemName");
        selectedPrice=category1Price;
        selectedCategory= category1Name;
        isFirstTimeAddToCart=sharedPreferences.getBoolean(buyItemName+selectedCategory,true);
        if (!isFirstTimeAddToCart) {
            cart.setText("Go to Cart");
        }
        SQLiteDatabase myDatabase = this.openOrCreateDatabase("KrishnaSweetsDatabase",MODE_PRIVATE,null);
         buy.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 buyIntent.putExtra("item",buyItemName);
                 buyIntent.putExtra("category",selectedCategory);
                 buyIntent.putExtra("price",Integer.parseInt(selectedPrice));
                 buyIntent.putExtra("qty",Integer.parseInt(buyQuantity.getText().toString()));
                 startActivity(buyIntent);
             }
         });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myDatabase.execSQL("Drop table OurCart");
//                Toast.makeText(BuyActivity.this, "I am done", Toast.LENGTH_SHORT).show();
                if (cart.getText().toString() != "Go to Cart"){
                    isFirstTimeAddToCart = sharedPreferences.getBoolean(buyItemName+selectedCategory, true);
                 if (isFirstTimeAddToCart) {
                    try {
                        myDatabase.execSQL("CREATE table IF NOT EXISTS OurCart (name Varchar(30) ,qty INTEGER,price INTEGER,category varchar(30),imageUrl VARCHAR(2083))");
                        myDatabase.execSQL("insert into OurCart (name,qty,price,category,imageUrl) values ('" + buyItemName + "',"+  Integer.parseInt(buyQuantity.getText().toString())   +",'" + selectedPrice + "','" + selectedCategory + "','" + url + "')");


//                        Log.i("data", "insert into OurCart (name,qty,price,category,imageUrl) values (" + buyItemName + ",1," + selectedPrice + "," + selectedCategory + "," + url + ")");
//                        Cursor c = myDatabase.rawQuery("Select * from OurCart", null);
//                        int nameIndex = c.getColumnIndex("name");
//                        int qtyIndex = c.getColumnIndex("qty");
//                        int priceIndex = c.getColumnIndex("price");
//                        int categoryIndex = c.getColumnIndex("category");
//                        int imageUrlIndex = c.getColumnIndex("imageUrl");
//                        c.moveToFirst();
//                        while (!c.isAfterLast()) {
//                            Log.i("name", c.getString(nameIndex));
//                            Log.i("qty", c.getString(qtyIndex));
//                            Log.i("price", c.getString(priceIndex));
//                            Log.i("category", c.getString(categoryIndex));
//                            Log.i("imageUrl", c.getString(imageUrlIndex));
//                            c.moveToNext();
                            sharedPreferences.edit().putBoolean(buyItemName+selectedCategory, false).apply();
//                            // 15:56
//                        }
                        cart.setText("Go to Cart");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(BuyActivity.this, "Unsuccessfull", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BuyActivity.this, "Already in Cart", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                startActivity(intent);
                    //isFirstTimeAddToCart = sharedPreferences.getBoolean(buyItemName+selectedCategory, true);
//                    if(isFirstTimeAddToCart){
//                        cart.setText("Add to Cart");
//                    }
                }
                }
        });


        itemName.setText(buyItemName);
        price.setText("Rs."+category1Price);
        category1Radio.setText(category1Name);
        if(category2Name.equals("")) {
        category2Radio.setVisibility(View.INVISIBLE);
        }
            else{
            category2Radio.setText(category2Name);
        }
        category1Radio.setChecked(true);
//        Picasso.get()
//                .load(url)
//                //.placeholder(R.mipmap.ic_launcher)
//                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
//                .centerCrop()
//                .fit()
//                .into(buyItemImage);

        Glide.with(this)
                .load(url) // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
              //  .override(200, 200) // resizing
                .fitCenter()
                .centerCrop()
                .into(buyItemImage);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.cashOnDelivery:
                        price.setText("Rs."+category1Price);
                        Toast.makeText(BuyActivity.this,category1Name , Toast.LENGTH_SHORT).show();
                        selectedPrice=category1Price;
                        selectedCategory= category1Name;
                        isFirstTimeAddToCart = sharedPreferences.getBoolean(buyItemName+selectedCategory, true);
                        if (!isFirstTimeAddToCart) {
                            cart.setText("Go to Cart");
                        }
                        else{
                            cart.setText("Add to Cart");
                        }

                           break;
                    case R.id.radioCategory2 :
                        price.setText("Rs."+category2Price);
                        Toast.makeText(BuyActivity.this,category2Name , Toast.LENGTH_SHORT).show();
                        selectedCategory=category2Name;
                        selectedPrice=category2Price;
                        isFirstTimeAddToCart = sharedPreferences.getBoolean(buyItemName+selectedCategory, true);
                        if (!isFirstTimeAddToCart) {
                            cart.setText("Go to Cart");
                        }
                        else{
                            cart.setText("Add to Cart");
                        }

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
            }
        });


    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here

        isFirstTimeAddToCart = sharedPreferences.getBoolean(buyItemName+selectedCategory, true);
                    if(isFirstTimeAddToCart){
                        cart.setText("Add to Cart");
                    }
    }
    public void ClickMenu(View view){
        //open drawer
        MainActivity2.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        //close Drawer
        MainActivity2.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        //close Drawer
        MainActivity2.redirectActivity(this,MainActivity.class);
    }
    public void ClickDashboard(View view){
        MainActivity2.redirectActivity(this,MainActivity.class);
    }
    public void ClickAboutUs(View view){
        MainActivity2.redirectActivity(this,AboutAdmin.class);
    }
    public void ClickLogout(View view){
       signOut();
    }
    public  void signOut() {
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //closeDrawer
        MainActivity2.closeDrawer(drawerLayout);
    }
    public void ClickCart(View view){
        MainActivity2.redirectActivity(this,CartActivity.class);
    }
}