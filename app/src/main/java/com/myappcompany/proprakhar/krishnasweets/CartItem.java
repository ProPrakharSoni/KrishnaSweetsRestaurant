package com.myappcompany.proprakhar.krishnasweets;

public class CartItem {

    private String mItemName,mUrl,mCategory;
    private int mQty,mPrice;
    public CartItem(){
    }
    public CartItem(String itemName,String url,String category,int qty,int price){
        mItemName=itemName;
        mUrl=url;
        mCategory=category;
        mQty=qty;
        mPrice=price;
    }
    public String getmItemName(){return mItemName;}
    public String getmUrl(){return mUrl;}
    public String getmCategory(){return mCategory;}
    public int getmQty(){return mQty;}
    public int getmPrice(){return mPrice;}

    public void setmItemName(String name){ mItemName=name;}
    public void setmUrl(String url){mUrl=url;}
    public void setmCategory(String cat){mCategory=cat;}
    public void setmQty(int qty){mQty=qty;}
    public void setmPrice(int price){mPrice=price;}
}
