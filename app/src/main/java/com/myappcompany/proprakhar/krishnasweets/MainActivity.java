package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

//    Button logout,mData;
//    GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button Pasta,Fried_Rice,Chinese,Chaumin,Burger,PavBaji,IceCream,Dhosa,Soup,ColdDrink,Bakery,Nasta,Sweets,Pizza,otherItems,offer,aboutUs;
    private Intent intent;
    private DatabaseReference mDatabaseRef;
    private TextView developer,txtMarquee,userName;
    private String name,movingText;
    private DrawerLayout drawerLayout;
    private ImageView userImage;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        txtMarquee =findViewById(R.id.marqueeText);
        drawerLayout = findViewById(R.id.drawerlayout);
           // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
            new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                                              .build());

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        adView = findViewById(R.id.ad_view);

        // Create an ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);
        //userImage=findViewById()
        userImage=findViewById(R.id.userImage);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        try {
            Glide.with(getApplicationContext())
                    .load(mAuth.getCurrentUser().getPhotoUrl())
                    .placeholder(R.color.common_google_signin_btn_text_light_disabled)
                    .override(1000, 200) // resizing
                    .centerInside()
                    .into(userImage);
        }catch(Exception e){
            e.printStackTrace();
        }
        userName=findViewById(R.id.userName);
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("");// In my experience i get that if I make once connection here (I don't use this variable here) then It takes less time to get data from firebase again when this method is used again;
        name=mAuth.getCurrentUser().getDisplayName();
        userName.setText(name);
        txtMarquee.setText("Hi "+name+" ,Welcome to Krishna Sweets App, Home delivery available above Rs.200 from 11AM to 9PM!!");
        aboutUs=findViewById(R.id.aboutUs);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AboutAdmin.class));
            }
        });
        intent = new Intent(getApplicationContext(),ItemActivity.class);
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


//        Glide.with(getApplicationContext())
//                .load(mAuth.getCurrentUser().getPhotoUrl()) // image url
//                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
//                .override(200, 200) // resizing
//                .centerInside()
//                .into(profile);
    }


    public void ClickMenu(View view){
        //open drawer
        MainActivity2.openDrawer(drawerLayout);
    }
    public void ClickCart(View view){
        MainActivity2.redirectActivity(this,CartActivity.class);
    }
    public void ClickLogo(View view){
        //close Drawer
        MainActivity2.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        //close Drawer

        MainActivity2.redirectActivity(this,MainActivity2.class);
    }
    public void ClickDashboard(View view){
//        MainActivity2.redirectActivity(this,MainActivity.class);
        MainActivity2.closeDrawer(drawerLayout);

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

}
