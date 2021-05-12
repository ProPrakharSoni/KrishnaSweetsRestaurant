package com.myappcompany.proprakhar.krishnasweets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class BuyActivity extends AppCompatActivity {
   // glide faster than picasso
    private ImageView buyItemImage;
    private RadioGroup radioGroup;
    private TextView itemName,price;
    private Button cart,buy;
    private RadioButton category1Radio,category2Radio;
    private String selectedCategory,selectedPrice,url;
    SharedPreferences sharedPreferences;
    private Boolean isFirstTimeAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        sharedPreferences=this.getSharedPreferences("com.myappcompany.proprakhar.krishnasweets", Context.MODE_PRIVATE);
        buyItemImage=findViewById(R.id.buyItemImage);
        radioGroup=findViewById(R.id.radioGroup);
        itemName=findViewById(R.id.buyItemName);
        price=findViewById(R.id.buyItemPrice);
        category1Radio=findViewById(R.id.radioCategory1);
        category2Radio=findViewById(R.id.radioCategory2);
        buy=findViewById(R.id.BuyNowButton);
        cart=findViewById(R.id.AddToCartButton);
        Intent i = getIntent();
         url =i.getStringExtra("url");
        String category1Name=i.getStringExtra("category1Name");
        String category1Price=i.getStringExtra("category1Price");
        String category2Name=i.getStringExtra("category2Name");
        String category2Price=i.getStringExtra("category2Price");
        String buyItemName=i.getStringExtra("itemName");
        isFirstTimeAddToCart=sharedPreferences.getBoolean(buyItemName,true);
        selectedPrice=category1Price;
        selectedCategory= category1Name;
        SQLiteDatabase myDatabase = this.openOrCreateDatabase("KrishnaSweetsDatabase",MODE_PRIVATE,null);
//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(BuyActivity.this, "I am working", Toast.LENGTH_SHORT).show();
//                if (isFirstTimeAddToCart) {
//                try {
//                    String ul="abc";
//                 //   String z="insert into OurCart (name,qty,price,category,imageUrl) value ("+buyItemName+",1,"+selectedPrice+","+selectedCategory+","+url+")";
//                    myDatabase.execSQL("CREATE table IF NOT EXISTS OurCart (name Varchar(30) ,qty INTEGER,price INTEGER,category varchar(30),imageUrl VARCHAR(2083))");
//                  //  myDatabase.execSQL("insert into OurCart (name,qty,price,category,imageUrl) values ("+buyItemName+",1,"+selectedPrice+","+selectedCategory+","+ul+")");
//                    myDatabase.execSQL("insert into OurCart (name,qty,price,category,imageUrl) values ("+buyItemName+",1,"+selectedPrice+","+ul+","+ul+")");
//
//                    // myDatabase.execSQL("insert into OurCart (name,qty,price,category,imageUrl) values ('prakhar',2,1,'son i','xyz')");
//
//                    Log.i("data","insert into OurCart (name,qty,price,category,imageUrl) values ("+buyItemName+",1,"+selectedPrice+","+selectedCategory+","+url+")");
//                    Cursor c = myDatabase.rawQuery("Select * from OurCart", null);
//                    int nameIndex = c.getColumnIndex("name");
//                    int qtyIndex = c.getColumnIndex("qty");
//                    int priceIndex = c.getColumnIndex("price");
//                    int categoryIndex = c.getColumnIndex("category");
//                    int imageUrlIndex = c.getColumnIndex("imageUrl");
//                    c.moveToFirst();
//                    while (!c.isAfterLast()) {
//                        Log.i("name", c.getString(nameIndex));
//                        Log.i("qty", c.getString(qtyIndex));
//                        Log.i("price", c.getString(priceIndex));
//                        Log.i("category", c.getString(categoryIndex));
//                        Log.i("imageUrl", c.getString(imageUrlIndex));
//                        c.moveToNext();
//                   //     sharedPreferences.edit().putBoolean(buyItemName,false).apply();
//                        //15:56
//                    }
//                    Toast.makeText(BuyActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(BuyActivity.this, "Unsuccessfull", Toast.LENGTH_SHORT).show();
//                }
//                 }
//                  else{
//                     Toast.makeText(BuyActivity.this, "Already in Cart", Toast.LENGTH_SHORT).show();
//                  }
//                }
//        });


        itemName.setText(buyItemName);
        price.setText(category1Price+" Rs");
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
                    case R.id.radioCategory1:
                        price.setText(category1Price+" Rs");
                        Toast.makeText(BuyActivity.this,category1Name , Toast.LENGTH_SHORT).show();
                        selectedPrice=category1Price;
                        selectedCategory= category1Name;

                           break;
                    case R.id.radioCategory2 :
                        price.setText(category2Price+" Rs");
                        Toast.makeText(BuyActivity.this,category2Name , Toast.LENGTH_SHORT).show();
                        selectedCategory=category2Name;
                        selectedPrice=category2Price;

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
            }
        });


    }

}