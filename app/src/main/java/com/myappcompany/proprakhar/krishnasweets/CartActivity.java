package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    Button buy;
    TextView totalMoney;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    RecyclerView.LayoutManager layoutManager;
    private List<CartItem> mCartItem;
    RecyclerView mRecyclerView;
    CartAdapter mAdapter;
    SQLiteDatabase myDatabase;
    SharedPreferences sharedPreferences;
    int totalPrice=0;
    private ImageView cartEmptyPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mCartItem= new ArrayList<>();
        int add;
        totalPrice=0;
        totalMoney=findViewById(R.id.totalPrice);
        cartEmptyPic=findViewById(R.id.cartEmptyPic);
        sharedPreferences=this.getSharedPreferences("com.myappcompany.proprakhar.krishnasweets", Context.MODE_PRIVATE);
       myDatabase = this.openOrCreateDatabase("KrishnaSweetsDatabase",MODE_PRIVATE,null);
       try {
           Cursor c = myDatabase.rawQuery("Select * from OurCart", null);
           int nameIndex = c.getColumnIndex("name");
           int qtyIndex = c.getColumnIndex("qty");
           int priceIndex = c.getColumnIndex("price");
           int categoryIndex = c.getColumnIndex("category");
           int imageUrlIndex = c.getColumnIndex("imageUrl");
           c.moveToFirst();
           while (!c.isAfterLast()) {
               CartItem cartItem = new CartItem(c.getString(nameIndex), c.getString(imageUrlIndex), c.getString(categoryIndex), c.getInt(qtyIndex), c.getInt(priceIndex));
//            Log.i("name", c.getString(nameIndex));
//            Log.i("qty", c.getString(qtyIndex));
//            Log.i("price", c.getString(priceIndex));
//            Log.i("category", c.getString(categoryIndex));
//            Log.i("imageUrl", c.getString(imageUrlIndex));
               add=c.getInt(qtyIndex)*c.getInt(priceIndex);
               totalPrice=totalPrice+add;
               c.moveToNext();
               // sharedPreferences.edit().putBoolean(buyItemName+selectedCategory, false).apply();
               // 15:56
               mCartItem.add(cartItem);
           }
           totalMoney.setText(Integer.toString(totalPrice));
           buy = findViewById(R.id.buy);
           mAuth = FirebaseAuth.getInstance();
           mRecyclerView = findViewById(R.id.cartRecyclerView);
           mRecyclerView.setHasFixedSize(true);
           mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
           mAdapter = new CartAdapter(CartActivity.this, mCartItem, CartActivity.this);
           mRecyclerView.setAdapter(mAdapter);
           buy.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   goToPayment();
                   }
           });

           GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                   .requestEmail()
                   .build();
           mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        logoutUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // Toast.makeText(CartActivity.this, "I am working", Toast.LENGTH_SHORT).show();
//                     signOut();
//            }
//        });
       }catch(Exception e){
           e.printStackTrace();
       }
        if(totalMoney.getText().toString().equals("0")){
            cartEmptyPic.setVisibility(View.VISIBLE);
        }
    }

    private void goToPayment() {
        if(totalMoney.getText().toString().equals("0")){
            Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
        }else {
           // Toast.makeText(CartActivity.this, totalMoney.getText().toString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onIncClick(int value,int position) {
        makeChange(value,position);
    }

    @Override
    public void onDecClick(int value,int position) {
        makeChange(value,position);
    }

    @Override
    public void onItemDelete(int position) {
        try {
            String deleteItemName = mCartItem.get(position).getmItemName();
            String delCategory = mCartItem.get(position).getmCategory();
            int qty =mCartItem.get(position).getmQty();
            totalPrice=totalPrice-(qty*mCartItem.get(position).getmPrice());
            sharedPreferences.edit().putBoolean(deleteItemName+delCategory, true).apply();
            totalMoney.setText(Integer.toString(totalPrice));
            myDatabase.execSQL("Delete from OurCart where name ='" + deleteItemName + "' AND  category = '"+delCategory + "' ");
            mCartItem.remove(position);
            mAdapter.notifyDataSetChanged();
            if(totalMoney.getText().toString().equals("0")){
                cartEmptyPic.setVisibility(View.VISIBLE);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void makeChange(int value,int position){
        String item  = mCartItem.get(position).getmItemName();
        String cat=mCartItem.get(position).getmCategory();
        int qty =mCartItem.get(position).getmQty();
        totalPrice=totalPrice-(qty*mCartItem.get(position).getmPrice());
        totalPrice=totalPrice+(value*mCartItem.get(position).getmPrice());
        totalMoney.setText(Integer.toString(totalPrice));
        myDatabase.execSQL("Update OurCart set qty = "+ value+" where name = '"+item +"' AND category ='"+cat+"' ");
        mCartItem.get(position).setmQty(value);
    }
}