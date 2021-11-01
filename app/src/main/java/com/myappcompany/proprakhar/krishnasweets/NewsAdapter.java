package com.myappcompany.proprakhar.krishnasweets;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{
    private Context mContext;
    private List<Upload> mUploads;
    private NewsFeedInterface recyclerViewClickInterface;

    final int radius = 50;
    final int margin = 5;
    final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        public NewsAdapter(Context context,List<Upload> uploads,NewsFeedInterface newsFeedInterface){
            mContext=context;
            mUploads=uploads;
            recyclerViewClickInterface=newsFeedInterface;
        }

        @NonNull
        @Override
        public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.i("mContext",mUploads.toString());
            View v= LayoutInflater.from(mContext).inflate(R.layout.news_feed,parent,false);
            return new NewsAdapter.MyViewHolder(v);
        }


        @Override
        public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {
            Upload uploadCurrent = mUploads.get(position);
            holder.mtime.setText(uploadCurrent.getCategory1());
            holder.mSomeInfo.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                //.placeholder(R.mipmap.ic_launcher)
                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
                .fit()
                .transform(transformation)
                .into(holder.mItemImage);
//            Glide.with(mContext)
//                    .load(uploadCurrent.getImageUrl()) // image url
//                    //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
//                    .override(200, 200) // resizing
//                    .fitCenter()
//                    .into(holder.mItemImage);

        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView mtime,mSomeInfo;
        public ImageView mItemImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtime=itemView.findViewById(R.id.timestamp);
            mSomeInfo=itemView.findViewById(R.id.someInfo);
            mItemImage= itemView.findViewById(R.id.itemImageforRecycle);

            // mItemImage=itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onNewsItemClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    recyclerViewClickInterface.onNewsItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
