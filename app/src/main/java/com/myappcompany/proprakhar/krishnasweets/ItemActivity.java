package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.grpc.Context;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import pl.droidsonroids.gif.GifImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    // Here data shown=>
    ImageView cart,profile;
    FirebaseAuth mAuth;
    RecyclerView mRecyclerView;
    ItemAdapter mAdapter;
    private int sizelist;
    private TextView itemCheck;
    RecyclerView.LayoutManager layoutManager;
    private List<Upload> mUploads;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage  mStorage;
    String email;
    private ValueEventListener mDBListener;
    Intent intent;
    GifImageView chefGif;
    FirebaseUser user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        chefGif=findViewById(R.id.chefGif);
        itemCheck=findViewById(R.id.itemCheck);
        mRecyclerView=findViewById(R.id.itemRecyclerView);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        email=user.getEmail();
        mRecyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        cart =findViewById(R.id.cart);
        profile=findViewById(R.id.profile);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // intent.putExtra("giveItem","");
                //startActivity(intent);
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });
//        mItemAdapter=new ItemAdapter(this);
       // mRecyclerView.setAdapter(mItemAdapter);
        mUploads=new ArrayList<>();
        mAdapter = new ItemAdapter(ItemActivity.this, mUploads,ItemActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        intent=new Intent(getApplicationContext(),BuyActivity.class);
        Intent intent = getIntent();
        String val =intent.getStringExtra("giveItem");
        //Toast.makeText(this, val, Toast.LENGTH_SHORT).show();
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
        mStorage=FirebaseStorage.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference(val);
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

//                    mAdapter = new ItemAdapter(ItemActivity.this, mUploads,ItemActivity.this);
//                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.notifyDataSetChanged();

                    chefGif.setVisibility(View.INVISIBLE);
                    sizelist=mUploads.size();
                    if(sizelist==0){
                        itemCheck.setVisibility(View.VISIBLE);
                    }
                  //  Toast.makeText(getApplicationContext(), Integer.toString(sizelist), Toast.LENGTH_SHORT).show();

                    //i think to save bitmap in an array.
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    chefGif.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
      //  Toast.makeText(this, "Method Works", Toast.LENGTH_SHORT).show();
      //  startActivities(new Intent(getApplicationContext(),BuyActivity.class));
        Upload getUploadObject = mUploads.get(position);
        String url = getUploadObject.getImageUrl();
       // Log.i("url",url);
        intent.putExtra("url",url);
        intent.putExtra("itemName",getUploadObject.getName());
        intent.putExtra("category1Price",getUploadObject.getCategory1Price());
        intent.putExtra("category1Name",getUploadObject.getCategory1());
        intent.putExtra("category2Price",getUploadObject.getCategory2Price());
        intent.putExtra("category2Name",getUploadObject.getCategory2());
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(int position) {

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
                                    Toast.makeText(ItemActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
    }

    @Override
    public void onIncClick(int value, int position) {

    }

    @Override
    public void onDecClick(int value,int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}