package com.myappcompany.proprakhar.krishnasweets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class AboutAdmin extends AppCompatActivity {

    private ImageView cart,profile;
    FirebaseAuth mAuth;
    private TextView info;
    String val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_admin);
        cart=findViewById(R.id.cart);

        mAuth = FirebaseAuth.getInstance();
        profile=findViewById(R.id.profile);
        info = findViewById(R.id.info);
        val = "Namaste\uD83D\uDE4F..";
        val =val.concat("\n\nWe welcome you to our Krishna Sweets.\n\nWe are very glad to see you here.\n\nIf you have a sweet tooth and love  desserts and want some tasty snacks then yes you are at the right place   -Bindki's one stop destination.We are working since more than 50 years in this domain and the Restaurant got established  with the aim to serve you mouth- watering variety of sweets and snacks. \n The best sweet shop in Bindki  has a wide variety collections of sweets, dry -fruits, gift-hampers for you and your dear ones to add sparkle to your celebration.we make delicious mithais, seeing which you will not be able to stop yourself.\n");
        val=val.concat("Not only sweets, but you can also order our famous snacks, bakery products and cakes. we bake birthday cakes, anniversary cakes , wedding cakes  & cakes   for every happy moment that you celebrate together with your loved ones.\n");
        val=val.concat("Snacks are the delicious light food that adds extra enthusiasm to your day. we bring snack for your special gatherings, occasions and festivals,so that you can treat your guests in a delicious manner.  We offers you a wide variety of street foods prepared with hygiene and precautionary measures so that you never compromise with your health .Krishna sweets Restaurant has tasty and delicious dishes for you in their menu. \n" +
                "So, whether you visit our outlets or order online from Krishna Sweet Shop and  Restaurant in Bindki  indulge yourself with the heavenly taste of your favorite delicacies. Keep ordering your choices from us and enjoy our food, because we maintain food safety during the COVID-19 pandemic.\n" +
                "\nHappy Eating!!\nKrishna Sweets Bindki\n\n");
        info.setMovementMethod(new ScrollingMovementMethod());
        info.setText(val);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
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
    }
}