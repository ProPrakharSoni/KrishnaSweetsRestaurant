package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    // Here data shown=>

    RecyclerView mRecyclerView;
    ItemAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<Upload> mUploads;
    private DatabaseReference mDatabaseRef;
    String Url;
    Intent intent;
    GifImageView chefGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        chefGif=findViewById(R.id.chefGif);
        mRecyclerView=findViewById(R.id.itemRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
//        mItemAdapter=new ItemAdapter(this);
       // mRecyclerView.setAdapter(mItemAdapter);
        mUploads=new ArrayList<>();
        intent=new Intent(getApplicationContext(),BuyActivity.class);
        Intent intent = getIntent();
        String val =intent.getStringExtra("giveItem");
        //Toast.makeText(this, val, Toast.LENGTH_SHORT).show();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference(val);
        try {
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSanpshot : snapshot.getChildren()) {
                        Upload upload = postSanpshot.getValue(Upload.class);
//                        Url=upload.getImageUrl();
//                        Log.i("Url",Url);
//                        Log.i("UrlMethod","Executes");
                        Log.i("Uploading",upload.getName());
                       Log.i("UploadingPrice",upload.getCategory1Price());
                        mUploads.add(upload);
                    }
                    mAdapter = new ItemAdapter(ItemActivity.this, mUploads,ItemActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    chefGif.setVisibility(View.INVISIBLE);
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
}