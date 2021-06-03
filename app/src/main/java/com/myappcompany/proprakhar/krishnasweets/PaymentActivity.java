package com.myappcompany.proprakhar.krishnasweets;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private  TextView summary,callText;
    private   SQLiteDatabase myDatabase;
    private  String itemSummary,item,category,shopNumber,message;
    private  GridView gridView;
    private  List<String> data;
    private  int totalPrice;
    private  RadioButton cash;
    private  ImageView call;
    private  Intent intent,buyIntent;
    private int qty,price;
    private Button placeOrder;
    private Boolean addressSave,isAppInstalled;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i=1;
        setContentView(R.layout.activity_payment);
        call=findViewById(R.id.callButton);
        callText=findViewById(R.id.callText);
        placeOrder=findViewById(R.id.placeOrder);
        myDatabase = this.openOrCreateDatabase("KrishnaSweetsDatabase", MODE_PRIVATE, null);
        sharedPreferences=this.getSharedPreferences("com.myappcompany.proprakhar.krishnasweets", Context.MODE_PRIVATE);
        addressSave=(!(sharedPreferences.getBoolean("newUser",true)));
        shopNumber="918960549442";
        callText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setData(Uri.parse("tel: 8960549442"));
                makeCall();
            }
        });
        message="*Order*\n";
        buyIntent=getIntent();
        String itemName="";
        gridView=findViewById(R.id.gridView);
        cash=findViewById(R.id.cashOnDelivery);
        buyIntent=getIntent();
        price=buyIntent.getIntExtra("price",-2);
        cash.setChecked(true);
        intent=new Intent(Intent.ACTION_CALL);
        itemSummary="";
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setData(Uri.parse("tel: 8960549442"));
                makeCall();
            }
        });
        data = new ArrayList<>();
        //message=message.concat("Sr.no  Item Name  Category  Qty  Price\n");
        data.add("Sr.no");
        data.add("Item Name");
        data.add("Category");
        data.add("Qty");
        data.add("Price");
        if(price==-2) {
            Cursor c = myDatabase.rawQuery("Select * from OurCart", null);
            int nameIndex = c.getColumnIndex("name");
            int qtyIndex = c.getColumnIndex("qty");
            int priceIndex = c.getColumnIndex("price");
            int categoryIndex = c.getColumnIndex("category");
            int imageUrlIndex = c.getColumnIndex("imageUrl");
//            data.add("Sr.no");
//            data.add("Item Name");
//            data.add("Category");
//            data.add("Quantity");
//            data.add("Price");
            c.moveToFirst();
            while (!c.isAfterLast()) {
                data.add(Integer.toString(i) + ".");
                data.add(c.getString(nameIndex));
                message=message.concat(Integer.toString(i) + ". *"+c.getString(nameIndex)+"* | ");
                data.add(c.getString(categoryIndex));
                message=message.concat(c.getString(categoryIndex)+" | ");
                data.add(Integer.toString(c.getInt(qtyIndex)));
                message=message.concat("Qty : "+Integer.toString(c.getInt(qtyIndex))+"\n");
                data.add(Integer.toString(c.getInt(qtyIndex) * c.getInt(priceIndex)) + " Rs");
             //   message=message.concat(Integer.toString(c.getInt(qtyIndex) * c.getInt(priceIndex)) + " Rs"+"."+"\n");
                totalPrice = totalPrice + (c.getInt(qtyIndex) * c.getInt(priceIndex));
                //   CartItem cartItem = new CartItem(c.getString(nameIndex), c.getString(imageUrlIndex), c.getString(categoryIndex), c.getInt(qtyIndex), c.getInt(priceIndex));
                // itemSummary=itemSummary.concat(Integer.toString(i)+" "+c.getString(nameIndex)+" Category:"+c.getString(categoryIndex)+" Qty:"+Integer.toString(c.getInt(qtyIndex))+" Price:"+c.getInt(priceIndex)+"\n");
                i++;
                c.moveToNext();
                // sharedPreferences.edit().putBoolean(buyItemName+selectedCategory, false).apply();
                // 15:56
            }
            i--;
            message=message.concat("\n*Total Items:* "+Integer.toString(i)+"       *Total Price:* "+Integer.toString(totalPrice)+" Rs\n");
            message=message.concat("\n*Customer Details* :\n");
           userData();
        }else{
            item=buyIntent.getStringExtra("item");
            message=message.concat(Integer.toString(i) + ". *"+item+"* | ");
            category=buyIntent.getStringExtra("category");
            message=message.concat(category+" | ");
            qty=buyIntent.getIntExtra("qty",1);
            message=message.concat("Qty : "+qty+"\n");
            totalPrice=price*qty;
            message=message.concat("\n*Total Items:* 1"+"                 *Total Price:* "+Integer.toString(totalPrice)+" Rs\n");
            message=message.concat("\n*Customer Details* :\n");
            userData();
            data.add("1");
            data.add(item);
            data.add(category);
            data.add(Integer.toString(qty));
            data.add(totalPrice+" Rs");
        }
            data.add("Total Price:");
            data.add("");
            data.add("");
            data.add("");
            data.add(Integer.toString(totalPrice)+" Rs");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
            gridView.setAdapter(adapter);

            placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(addressSave){
                       // Toast.makeText(PaymentActivity.this, "Address Saved", Toast.LENGTH_SHORT).show();
                    isAppInstalled=isInstalled("com.whatsapp");
                    if(isAppInstalled){
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://api.whatsapp.com/send?phone="+shopNumber+"&text="+ message));
                        startActivity(i);
                    }else{
                        Toast.makeText(PaymentActivity.this, "Please Install WhatsAPP", Toast.LENGTH_SHORT).show();
                    }
                    }else{
                        Toast.makeText(PaymentActivity.this, "Please fill your address", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),UserProfile.class));
                        addressSave=(!(sharedPreferences.getBoolean("newUser",true)));
                    }
                }
            });
    }

    private void userData() {
        try {
            Cursor cursor = myDatabase.rawQuery("Select * from UserProfile", null);
            int name = cursor.getColumnIndex("name");
            int phoneIndex = cursor.getColumnIndex("phone");
            int addressIndex = cursor.getColumnIndex("address");
            int pinCodeIndex = cursor.getColumnIndex("pinCode");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                message=message.concat("Customer name : *"+cursor.getString(name)+"*\n");
                message=message.concat("Phone no. : "+cursor.getString(phoneIndex)+"\n");
                message=message.concat("PinCode : *"+Integer.toString(cursor.getInt(pinCodeIndex))+"*\n");
                message=message.concat("Address : *"+cursor.getString(addressIndex)+"*\n");
//                    name.setText(c.getString(name));
//                    phone.setText(c.getString(phoneIndex));
//                    address.setText(c.getString(addressIndex));
//                    pinCode.setText(Integer.toString(c.getInt(pinCodeIndex)));
                cursor.moveToNext();
//                 sharedPreferences.edit().putBoolean(buyItemName+selectedCategory, false).apply();
//                 15:56
//                 mCartItem.add(cartItem);
            }
            message=message.concat("\n*Cash On Delivery* ->  *"+Integer.toString(totalPrice)+"* Rs");
        }catch (Exception e){
            Toast.makeText(this, "catch", Toast.LENGTH_SHORT).show();
            //newUser=true;
            e.printStackTrace();
        }
    }

    private boolean isInstalled(String s){
     PackageManager packageManager=getPackageManager();
     boolean isInstalled;
     try{

         packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
         isInstalled=true;

     }catch(Exception e){

         isInstalled= false;
         e.printStackTrace();

     }
     return isInstalled;
    }

    private void makeCall() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
            startActivity(intent);
        }else{
            startActivity(intent);
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        addressSave=(!(sharedPreferences.getBoolean("newUser",true)));
       // Toast.makeText(this, "Im am working Hello", Toast.LENGTH_SHORT).show();
    }
}