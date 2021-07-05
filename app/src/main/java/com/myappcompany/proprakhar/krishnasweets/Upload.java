package com.myappcompany.proprakhar.krishnasweets;

import com.google.firebase.database.Exclude;

public class Upload {

    private  String mItemName,mCategory1,mCategory2,mImageUrl;
    private String mPriceCategory1,mPriceCategory2;
    private String mKey;

    public Upload(){
        //Empty constructor needed.
    }
    public  Upload(String itemName,String category1,String category2,String imageUrl,String priceCategory1 ,String priceCategory2 ){
      mItemName=itemName;

      mCategory1=category1;
      mCategory2=category2;
      mImageUrl=imageUrl;
      mPriceCategory1=priceCategory1;
      mPriceCategory2=priceCategory2;
    }
    public String getName(){
        return mItemName;
    }
    public String getCategory1 (){
        return mCategory1;
    }
    public String getCategory2(){
        return mCategory2;
    }
    public String getImageUrl(){
        return mImageUrl;
    }
    public String getCategory1Price(){
        return mPriceCategory1;
    }

    public String getCategory2Price(){
        return mPriceCategory2;
    }
    // these set functions are very important they are set the values ::
    public void setName(String name){
        mItemName=name;
    }
    public void setImageUrl(String imageUrl){
        mImageUrl=imageUrl;
    }
    public void setCategory1(String category1){
        mCategory1=category1;
    }
    public void setCategory2(String category2){
        mCategory2=category2;
    }
    public void setCategory1Price(String category1Price){
        mPriceCategory1=category1Price;
    }
    public void setcategory2Price(String category2Price){
        mPriceCategory2=category2Price;
    }
    @Exclude
    public String getKey(){
        return mKey;
    }
    @Exclude
    public void setKey(String key){
        mKey=key;
    }
}
