package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import com.google.firebase.messaging.FirebaseMessaging;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

//    Button logout,mData;
//    GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private ImageView cart,profile;
    private Button Pasta,Fried_Rice,Chinese,Chaumin,Burger,PavBaji,IceCream,Dhosa,Soup,ColdDrink,Bakery,Nasta,Sweets,Pizza,otherItems,offer,aboutUs;
    private Intent intent;
    private DatabaseReference mDatabaseRef;
    private TextView developer,txtMarquee;
    private String name,movingText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMarquee =findViewById(R.id.marqueeText);

        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("");// In my experience i get that if I make once connection here (I don't use this variable here) then It takes less time to get data from firebase again when this method is used again;
        mAuth = FirebaseAuth.getInstance();
        name=mAuth.getCurrentUser().getDisplayName();
        txtMarquee.setText("Hi "+name+" ,Welcome to Krishna Sweets App, Home delivery available above Rs.200 from 11AM to 9PM!!");
        aboutUs=findViewById(R.id.aboutUs);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AboutAdmin.class));
            }
        });
        intent = new Intent(getApplicationContext(),ItemActivity.class);
        cart =findViewById(R.id.cart);
    profile=findViewById(R.id.profile);
    developer = findViewById(R.id.developer);
    developer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),Developer.class));
        }
    });
    FirebaseMessaging.getInstance().subscribeToTopic("notification");
    otherItems=findViewById(R.id.otherItems);
    offer=findViewById(R.id.offers);
    profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // intent.putExtra("giveItem","");
            //startActivity(intent);
            startActivity(new Intent(getApplicationContext(),UserProfile.class));
        }
    });
    Pasta=findViewById(R.id.Pasta);
    Pasta.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Pasta");
            startActivity(intent);
        }
    });
    otherItems.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Other Items");
            startActivity(intent);
        }
    });
    offer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Offers");
            startActivity(intent);
        }
    });
    Fried_Rice=findViewById(R.id.Rice);
    Fried_Rice.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","FriedRice");
            startActivity(intent);
        }
    });
    Chinese=findViewById(R.id.Chinese);
    Chinese.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Chinese");
            startActivity(intent);
        }
    });
    Chaumin=findViewById(R.id.Chaumin);
    Chaumin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Noodles");
            startActivity(intent);
        }
    });
    Burger=findViewById(R.id.Burger);
    Burger.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Burger");
            startActivity(intent);
        }
    });
    PavBaji=findViewById(R.id.Pav_Baji);
    PavBaji.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","PavBaji");
            startActivity(intent);
        }
    });
    IceCream=findViewById(R.id.Ice_Cream);
    IceCream.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","IceCream");
            startActivity(intent);
        }
    });
    Dhosa=findViewById(R.id.Dhosa);
    Dhosa.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Dhhosa");
            startActivity(intent);
        }
    });
    Soup=findViewById(R.id.Soup);
    Soup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Soup");
            startActivity(intent);
        }
    });
    ColdDrink=findViewById(R.id.Cold_Drink);
    ColdDrink.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","ColdDrink");
            startActivity(intent);
        }
    });
    Bakery=findViewById(R.id.Bakery);
    Bakery.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Bakery");
            startActivity(intent);
        }
    });
    Nasta=findViewById(R.id.Nasta);
    Nasta.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Nasta");
            startActivity(intent);
        }
    });
    Sweets=findViewById(R.id.Sweets);
    Sweets.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Sweets");
            startActivity(intent);
        }
    });
    Pizza=findViewById(R.id.Pizza);
    Pizza.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("giveItem","Pizza");
            startActivity(intent);
        }
    });
    cart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),CartActivity.class));
        }
    });
        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                //.placeholder(R.mipmap.ic_launcher)
                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
                .centerInside()
                .fit()
                .transform(new CropCircleTransformation())
                .into(profile);
        Picasso.get()
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSV7VoOgaHUhoeKNRFDpyL1D__B72rfkIuFrA&usqp=CAU")
                //.placeholder(R.mipmap.ic_launcher)
                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
                .centerInside()
                .fit()
                .transform(new CropCircleTransformation())
                .into(cart);

//        Glide.with(getApplicationContext())
//                .load(mAuth.getCurrentUser().getPhotoUrl()) // image url
//                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
//                .override(200, 200) // resizing
//                .centerInside()
//                .into(profile);
    }



}
