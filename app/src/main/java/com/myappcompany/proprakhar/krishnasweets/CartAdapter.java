package com.myappcompany.proprakhar.krishnasweets;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mCartContext;
    private List<CartItem> mCartItems;
    private SQLiteDatabase myDatabase;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    public CartAdapter(Context context,List<CartItem> items,RecyclerViewClickInterface recyclerViewClickInterface1){
        mCartContext=context;
        mCartItems=items;
        recyclerViewClickInterface=recyclerViewClickInterface1;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mCartContext).inflate(R.layout.cart_view,parent,false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartCurrent = mCartItems.get(position);
        holder.mItemName.setText(cartCurrent.getmItemName());
        holder.mPrice.setText("Price: Rs."+Integer.toString(cartCurrent.getmPrice()));
        holder.mCategory.setText("Category: "+cartCurrent.getmCategory());
        holder.mQty.setText(Integer.toString(cartCurrent.getmQty()));
        Glide.with(mCartContext)
                .load(cartCurrent.getmUrl()) // image url
                //.placeholder(R.mipmap.ic_launcher) // any placeholder to load at start
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.mItemImage);
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

    public TextView mPrice,mItemName,mCategory;
    public ImageView mItemImage,mDelete;
    public TextView mQty;
    public Button inc,dec;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        inc=itemView.findViewById(R.id.itemInc);
        dec=itemView.findViewById(R.id.itemDec);
        mPrice=itemView.findViewById(R.id.cartitemprice);
        mItemName=itemView.findViewById(R.id.cartItemName);
        mCategory=itemView.findViewById(R.id.cartItemCategory);
        mItemImage=itemView.findViewById(R.id.cartItemImage);
        mQty=itemView.findViewById(R.id.buyQuantity);
        mDelete=itemView.findViewById(R.id.cartItemDelete);
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
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mQty.setText(Integer.toString(mCartItems.get(getAdapterPosition()).getmQty() + 1));
                recyclerViewClickInterface.onIncClick(Integer.parseInt(mQty.getText().toString()),getAdapterPosition());
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(Integer.parseInt(mQty.getText().toString())>1) {
                        mQty.setText(Integer.toString(mCartItems.get(getAdapterPosition()).getmQty() - 1));
                        recyclerViewClickInterface.onDecClick(Integer.parseInt(mQty.getText().toString()),getAdapterPosition());
                    }
                }
        });
               mDelete.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       recyclerViewClickInterface.onItemDelete(getAdapterPosition());
                   }
               });

    }
    }
}
