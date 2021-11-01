package com.myappcompany.proprakhar.krishnasweets;

public interface NewsFeedInterface {
    void onNewsItemClick(int position);
    void onNewsItemLongClick(int position);
    void onNewsIncClick(int value,int position);
    void onNewsDecClick(int value,int position);
    void onNewsItemDelete(int position);
}
