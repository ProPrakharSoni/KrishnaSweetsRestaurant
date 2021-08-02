package com.myappcompany.proprakhar.krishnasweets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    public ItemAdapter(Context context,List<Upload> uploads,RecyclerViewClickInterface recyclerViewClickInterface1){
        mContext=context;
        mUploads=uploads;
        recyclerViewClickInterface=recyclerViewClickInterface1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.mPrice.setText("Rs."+uploadCurrent.getCategory1Price());
        holder.mItemName.setText(uploadCurrent.getName());
//        Picasso.get()
//                .load(uploadCurrent.getImageUrl())
//                //.placeholder(R.mipmap.ic_launcher)
//                .placeholder(R.color.common_google_signin_btn_text_light_disabled)
//                .centerCrop()
//                .fit()
//                .into(holder.mItemImage);
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl()) // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.mItemImage);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView mPrice,mItemName;
        public ImageView mItemImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mPrice=itemView.findViewById(R.id.itemPriceRecyclerView);
            mItemName=itemView.findViewById(R.id.itemNameRecycle);
            mItemImage= itemView.findViewById(R.id.itemImageforRecycle);

           // mItemImage=itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    recyclerViewClickInterface.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
