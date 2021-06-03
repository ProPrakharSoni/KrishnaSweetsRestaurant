package com.myappcompany.proprakhar.krishnasweets;

public interface RecyclerViewClickInterface {

    void onItemClick(int position);
    void onItemLongClick(int position);
    void onIncClick(int value,int position);
    void onDecClick(int value,int position);
    void onItemDelete(int position);

}
