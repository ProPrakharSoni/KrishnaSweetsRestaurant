package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements RecyclerViewClickInterface, NewsFeedInterface {

    DrawerLayout drawerLayout;
    private ImageView userImage,pizza,burger,noodles,sweets,cakes,soup,samosa,friedRice,gift1,gift2,gift3,gift4,gift5,gift6,gift7,gift8,dosa;
    private TextView userName,aboutShop;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Intent intent;
    private RecyclerView mRecyclerView,giftRecyclerView;
    private NewsAdapter mAdapter;
    private ExtraFrontAdapter mGiftAdapter;
    private List<Upload> mUploads,mGifts;
    private DatabaseReference mDatabaseRef,mGiftDatabaseRef;
  //  private RecyclerView.LayoutManager layoutManager;
    private ValueEventListener mDBListener,mGiftDBListener;
    private AdView adView;
    private TextView viewMore;
    private Intent viewMoreIntent,buyIntent;
    private FirebaseStorage mStorage;
    private String email;
    //private HorizontalScrollView s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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


        mStorage=FirebaseStorage.getInstance();
        viewMoreIntent = new Intent(getApplicationContext(),ItemActivity.class);
        buyIntent=new Intent(getApplicationContext(),BuyActivity.class);
        viewMore=findViewById(R.id.viewMore);
        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("giveItem","Other Items");
                startActivity(intent);
            }
        });
           intent = new Intent(getApplicationContext(),ItemActivity.class);
        mAuth = FirebaseAuth.getInstance();
        email=mAuth.getCurrentUser().getEmail();
        aboutShop=findViewById(R.id.aboutKrishnaSweets);
        mRecyclerView=findViewById(R.id.newsRecycle);
        mRecyclerView.setHasFixedSize(true);
        giftRecyclerView=findViewById(R.id.giftRecycle);
       // giftRecyclerView.setHasFixedSize(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager giftLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
       // layoutManager=new GridLayoutManager(this,2);
        mUploads=new ArrayList<>();
        mGifts=new ArrayList<>();
        mAdapter = new NewsAdapter(MainActivity2.this, mUploads,MainActivity2.this);
        mGiftAdapter = new ExtraFrontAdapter(MainActivity2.this, mGifts,MainActivity2.this);
        giftRecyclerView.setAdapter(mGiftAdapter);
        giftRecyclerView.setLayoutManager(giftLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

       mRecyclerView.setNestedScrollingEnabled(false);

        drawerLayout = findViewById(R.id.drawerlayout);
        userImage=findViewById(R.id.userImage);
        userName=findViewById(R.id.userName);
        pizza = findViewById(R.id.pizza);
        burger= findViewById(R.id.burger);
        dosa=findViewById(R.id.dosa);
        noodles = findViewById(R.id.noodles);
        sweets = findViewById(R.id.sweets);
        cakes = findViewById(R.id.cakes);
        soup = findViewById(R.id.soup);
        samosa = findViewById(R.id.samosa);
        friedRice = findViewById(R.id.friedRice);
        String val;
        val = "Namaste\uD83D\uDE4F..";
        val =val.concat("\n\nWe welcome you to our Krishna Sweets.\n\nWe are very glad to see you here.\n\nIf you have a sweet tooth and love  desserts and want some tasty snacks then yes you are at the right place   -Bindki's one stop destination Krishna Sweets.We are working since more than 25 years in this domain and the Restaurant got established  with the aim to serve you mouth- watering variety of sweets and snacks. \n The best sweet shop in Bindki  has a wide variety collections of sweets, dry -fruits, gift-hampers for you and your dear ones to add sparkle to your celebration.we make delicious mithais, seeing which you will not be able to stop yourself.\n");
        val=val.concat("Not only sweets, but you can also order our famous snacks, bakery products and cakes. we bake birthday cakes, anniversary cakes , wedding cakes  & cakes   for every happy moment that you celebrate together with your loved ones.\n");
        val=val.concat("Snacks are the delicious light food that adds extra enthusiasm to your day. we bring snack for your special gatherings, occasions and festivals,so that you can treat your guests in a delicious manner.  We offers you a wide variety of street foods prepared with hygiene and precautionary measures so that you never compromise with your health .Krishna sweets Restaurant has tasty and delicious dishes for you in their menu. \n" +
                "So, whether you visit our outlets or order online from Krishna Sweet Shop and  Restaurant in Bindki  indulge yourself with the heavenly taste of your favorite delicacies. Keep ordering your choices from us and enjoy our food, because we maintain food safety during the COVID-19 pandemic.\n" +
                "\nHappy Eating!!\nKrishna Sweets Bindki\n\n");
        aboutShop.setText(val);

        //
        mGiftDatabaseRef=FirebaseDatabase.getInstance().getReference("Other Items");
        try {
            mGiftDBListener=   mGiftDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    mGifts.clear();

                    for (DataSnapshot postSanpshot : snapshot.getChildren()) {
                        Upload upload = postSanpshot.getValue(Upload.class);
//                        Url=upload.getImageUrl();
//                        Log.i("Url",Url);
//                        Log.i("UrlMethod","Executes");
                        upload.setKey(postSanpshot.getKey());
//                        Log.i("Uploading",upload.getName());
//                       Log.i("UploadingPrice",upload.getCategory1Price());
                        mGifts.add(upload);
                    }


                    mGiftAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // chefGif.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Create Post");
        try {
            mDBListener=   mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    mUploads.clear();

                    for (DataSnapshot postSanpshot : snapshot.getChildren()) {
                        Upload upload = postSanpshot.getValue(Upload.class);
//                        Url=upload.getImageUrl();
//                        Log.i("Url",Url);
//                        Log.i("UrlMethod","Executes");
                        upload.setKey(postSanpshot.getKey());
//                        Log.i("Uploading",upload.getName());
//                       Log.i("UploadingPrice",upload.getCategory1Price());
                        mUploads.add(upload);
                    }

                    Collections.reverse(mUploads);
                    mAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                   // chefGif.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            Glide.with(getApplicationContext())
                    .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/pizza_resize.png?raw=true") // image url
                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                    .override(1000, 700) // resizing
                    .centerInside()
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                    .into(pizza);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/burger_resize.jpeg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .into(burger);
         }catch(Exception e){
            e.printStackTrace();
        }
         try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/noodles_resize.jpg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                   .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                   .into(noodles);
         }catch(Exception e){
            e.printStackTrace();
        }
         try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/sweets_resize.jpg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .into(sweets);
         }catch(Exception e){
            e.printStackTrace();
        }
         try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/cake_resize_pro.jpg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .into(cakes);
         }catch(Exception e){
            e.printStackTrace();
        }
         try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/soup_resize.jpg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .into(soup);
         }catch(Exception e){
            e.printStackTrace();
        }
        try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/samosa_resize.jpg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .into(samosa);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/fried-rice_resize.jpg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                   .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                   .into(friedRice);
       }catch(Exception e){
            e.printStackTrace();
        }
         try{
           Glide.with(getApplicationContext())
                .load("https://github.com/ProPrakharSoni/Images/blob/master/resize/dosa_resized.jpg?raw=true") // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(1000, 700) // resizing
                   .centerInside()
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                   .into(dosa);
       }catch(Exception e){
            e.printStackTrace();
        }
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
        userName.setText(mAuth.getCurrentUser().getDisplayName());
      pizza.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","Pizza");
              startActivity(intent);
          }
      });
      burger.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","Burger");
              startActivity(intent);
          }
      });
      noodles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("giveItem","Noodles");
                startActivity(intent);
            }
        });
      cakes.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","Bakery");
              startActivity(intent);
          }
      });
      sweets.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","Sweets");
              startActivity(intent);
          }
      });
      soup.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","Soup");
              startActivity(intent);
          }
      });
      samosa.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","Nasta");
              startActivity(intent);
          }
      });
      friedRice.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","FriedRice");
              startActivity(intent);
          }
      });
      dosa.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              intent.putExtra("giveItem","Dhhosa");
              startActivity(intent);
          }
      });





    }

    public String getUserName(){
        return mAuth.getCurrentUser().getDisplayName();
    }
    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }
    public void ClickCart(View view){
        redirectActivity(this,CartActivity.class);
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        //Recreate activity
        //recreate();
        closeDrawer(drawerLayout);
    }
    public void ClickDashboard(View view){
        redirectActivity(this,MainActivity.class);
    }

    public void ClickAboutUs(View view){
        redirectActivity(this,AboutAdmin.class);
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
    public static void redirectActivity(Activity activity,Class aClass) {

        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
        closeDrawer(drawerLayout);
    }
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
        mGiftDatabaseRef.removeEventListener(mGiftDBListener);
    }
    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onItemClick(int position) {
        Upload getUploadObject = mGifts.get(position);
        String url = getUploadObject.getImageUrl();
        // Log.i("url",url);
        buyIntent.putExtra("url",url);
        buyIntent.putExtra("itemName",getUploadObject.getName());
        buyIntent.putExtra("category1Price",getUploadObject.getCategory1Price());
        buyIntent.putExtra("category1Name",getUploadObject.getCategory1());
        buyIntent.putExtra("category2Price",getUploadObject.getCategory2Price());
        buyIntent.putExtra("category2Name",getUploadObject.getCategory2());
        startActivity(buyIntent);
    }

    @Override
    public void onItemLongClick(int position) {
    }

    @Override
    public void onIncClick(int value, int position) {

    }

    @Override
    public void onDecClick(int value, int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onNewsItemClick(int position) {

    }

    @Override
    public void onNewsItemLongClick(int position) {
        if(email.equals("krishnasweetftp@gmail.com")||email.equals("ashish.krishna2014@gmail.com")||email.equals("prakhar.amansoni@gmail.com")||email.equals("paritsolutions@gmail.com")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do you definitely want to delete this")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Upload selectedItem = mUploads.get(position);
                            String selectedKey = selectedItem.getKey();
                            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                     mDatabaseRef.child(selectedKey).removeValue();
                                    Toast.makeText(MainActivity2.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
    }

    @Override
    public void onNewsIncClick(int value, int position) {

    }

    @Override
    public void onNewsDecClick(int value, int position) {

    }

    @Override
    public void onNewsItemDelete(int position) {

    }
}