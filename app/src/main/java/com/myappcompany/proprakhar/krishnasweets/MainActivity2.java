package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

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
    private AdView mAdView;
    private TextView viewMore;
    private Intent viewMoreIntent;
    //private HorizontalScrollView s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
//        MobileAds.initialize(getApplicationContext(),
//                "ca-app-pub-3940256099942544~3347511713");
//        MobileAds.i
        mAdView = findViewById(R.id.adView);
        mAdView = new AdView(this);

        mAdView.setAdSize(AdSize.BANNER);

        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
// TODO: Add adView to your view hierarchy.
        viewMoreIntent = new Intent(getApplicationContext(),ItemActivity.class);
        viewMore=findViewById(R.id.viewMore);
        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("giveItem","Other Items");
                startActivity(intent);
            }
        });
        //
        //test device

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);//
        String deviceId = md5(android_id).toUpperCase();//
     //   adRequest.addTestDevice(deviceId);

       // boolean isTestDevice = adRequest.isTestDevice(this);

        // Log.v(TAG, "is Admob Test Device ? "+deviceId+" "+isTestDevice); //to confirm it worked

        Log.i("device_id",deviceId);


        //..............

//        List<String> testDeviceIds = Arrays.asList(deviceId);//
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();//
//        MobileAds.setRequestConfiguration(configuration);//

        AdRequest adRequest = new AdRequest.Builder().build();

//        List<String> testDeviceIds = Arrays.asList(deviceId);
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        MobileAds.setRequestConfiguration(configuration);


 //       adRequest.addTestDevice(deviceId);

//        List<String> testDeviceIds = Arrays.asList(deviceId);//
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();//
//        MobileAds.setRequestConfiguration(configuration);//

        getUIDs();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.i("aderror",adError.toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        intent = new Intent(getApplicationContext(),ItemActivity.class);
        mAuth = FirebaseAuth.getInstance();
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
        mAdapter = new NewsAdapter(MainActivity2.this, mUploads);
        mGiftAdapter = new ExtraFrontAdapter(MainActivity2.this, mGifts);
        giftRecyclerView.setAdapter(mGiftAdapter);
        giftRecyclerView.setLayoutManager(giftLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
//        gift1=findViewById(R.id.gift1);
//        gift2=findViewById(R.id.gift2);
//        gift3=findViewById(R.id.gift3);
//        gift4=findViewById(R.id.gift4);
//        gift5=findViewById(R.id.gift5);
//        gift6=findViewById(R.id.gift6);
//        gift7=findViewById(R.id.gift7);
//        gift8=findViewById(R.id.gift8);
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

        //
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://assetscdn1.paytm.com/images/catalog/product/F/FA/FASCADBURY-CELEGIFT23761BDA1A2B1/1594625784636_0..jpg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift1);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://www.haldiram.com/assets/images/products/Soan-Papdi-veg31.jpg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift2);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://m.media-amazon.com/images/I/714ZqXhzXNL._SL1500_.jpg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift3);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://4.imimg.com/data4/SH/SD/ANDROID-10569818/product-500x500.jpeg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift4);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://m.media-amazon.com/images/I/71Taex6WC8L._SL1500_.jpg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift5);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://www.bigbasket.com/media/uploads/p/xxl/40157246-2_2-cadbury-dairy-milk-silk-hazelnut-chocolate-bar.jpg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift6);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://m.media-amazon.com/images/I/71vTZfNgMzL._SL1500_.jpg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift7);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try {
//            Glide.with(getApplicationContext())
//                    .load("https://m.media-amazon.com/images/I/41pHqT80UVL.jpg") // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(1000, 700) // resizing
//                    .centerInside()
//                    .into(gift8);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        try {
            Glide.with(getApplicationContext())
                    .load("https://static.toiimg.com/photo/51892394.cms") // image url
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
                .load("https://images.unsplash.com/photo-1571091718767-18b5b1457add?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8YnVyZ2VyfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80") // image url
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
                .load("https://www.whiskaffair.com/wp-content/uploads/2020/03/Hakka-Noodles-2-3.jpg") // image url
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
                .load("https://www.rajbhog.com/wp-content/uploads/2021/03/10-Must-Try-Rajbhog-Sweets-870x635.jpg") // image url
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
                .load("https://i.pinimg.com/originals/ab/67/53/ab6753ec1cef75f1cc2052487b1f4059.jpg") // image url
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
                .load("https://www.inspiredtaste.net/wp-content/uploads/2018/10/Homemade-Vegetable-Soup-Recipe-2-1200.jpg") // image url
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
                .load("https://www.simplyrecipes.com/thmb/UzO3UxgjYRECShzMe4eIliy4gis=/2000x1333/filters:fill(auto,1)/__opt__aboutcom__coeus__resources__content_migration__simply_recipes__uploads__2019__12__Vegetable-Samosas-LEAD-5-502b04ead41c4c7e91e1a214f1d9d939.jpg") // image url
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
                .load("https://www.whiskaffair.com/wp-content/uploads/2017/11/Schezwan-Egg-Fried-Rice-3.jpg") // image url
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
                .load("https://thumbs.dreamstime.com/b/masala-dosa-21646814.jpg") // image url
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
//        s=findViewById(R.id.horizontalScrollView);
//        s.postDelayed(new Runnable() {
//            public void run() {
//                s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            }
//        }, 100L);
//        s.post(new Runnable() {
//            public void run() {
//                s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            }
//        });
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                ((HorizontalScrollView) Iview
//                        .findViewById(R.id.s))
//                        .fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            }
//        });
//        s.post(new Runnable() {
//            @Override
//            public void run() {
//                s.fullScroll(View.FOCUS_RIGHT);
//            }
//        });





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
        super.onPause();
        closeDrawer(drawerLayout);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
        mGiftDatabaseRef.removeEventListener(mGiftDBListener);
    }



    //testing
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
           // Logger.logStackTrace(TAG,e);
        }
        return "";
    }
    // testing end



    void getUIDs()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                    String myId = adInfo != null ? adInfo.getId() : null;

                    Log.i("UIDMY", myId);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "error occurred ", Toast.LENGTH_SHORT);
                  //  toast.setGravity(gravity, 0,0);
                    toast.show();
                }
            }
        });
    }
}