package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private  TextView callText,summaryText,callText2;
    private   SQLiteDatabase myDatabase;
    private  String itemSummary,item,category,shopNumber,message;
    private  int totalPrice;
    private  RadioButton cash;
    private  ImageView call,call2;
    private  Intent intent,buyIntent,intent2;
    private int qty,price,add;
    private Button placeOrder;
    private Boolean addressSave,isAppInstalled;
    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int i=1;
        add=1;
        setContentView(R.layout.activity_payment);
        call=findViewById(R.id.callButton);
        call2=findViewById(R.id.callButton2);
        callText=findViewById(R.id.callText);
        callText2=findViewById(R.id.callText2);
        summaryText=findViewById(R.id.orderSummary);
        //
//        ZonedDateTime zdt = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
//        }
        Date todayDate = new Date();
        int currentHour =  todayDate.getHours();
       // Log.i("currentTimes",Integer.toString(currentHour));


        //not work on old android
       // String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        //String timeNow[] = currentTime.split(":");

        // Log.i("currenttime","this"+currentTime);
      // Log.i("currentTimeh",timeNow[0]);
        //int currentHour=Integer.parseInt(timeNow[0]);
        summaryText.setMovementMethod(new ScrollingMovementMethod());
        placeOrder=findViewById(R.id.placeOrder);
        myDatabase = this.openOrCreateDatabase("KrishnaSweetsDatabase", MODE_PRIVATE, null);
        sharedPreferences=this.getSharedPreferences("com.myappcompany.proprakhar.krishnasweets", Context.MODE_PRIVATE);
        addressSave=(!(sharedPreferences.getBoolean("newUser",true)));
        shopNumber="919554137222";

        //

        //
        callText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                intent.setData(Uri.parse("tel: 8960549442"));
                makeCall();
            }
        });
        callText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall2();
            }
        });
        message="*Order*\n";
        buyIntent=getIntent();
        String itemName="";
       // gridView=findViewById(R.id.gridView);
        cash=findViewById(R.id.cashOnDelivery);
        buyIntent=getIntent();
        price=buyIntent.getIntExtra("price",-2);
        cash.setChecked(true);
        intent=new Intent(Intent.ACTION_CALL);
        intent2=new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:9554137222"));
        intent2.setData(Uri.parse("tel:9076607113"));
        itemSummary="";
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                intent.setData(Uri.parse("tel: 8960549442"));
                makeCall();
            }
        });
        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               makeCall2();
            }
        });
        //message=message.concat("Sr.no  Item Name  Category  Qty  Price\n");

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

                itemSummary=itemSummary.concat(Integer.toString(i) + ".  ");

                message=message.concat(Integer.toString(i) + ". *"+c.getString(nameIndex)+"* | ");
                itemSummary=itemSummary.concat("Item: "+c.getString(nameIndex)+"\n     Quantity: "+ Integer.toString(c.getInt(qtyIndex))  +"    Price: ");

                message=message.concat(c.getString(categoryIndex)+" | ");

                message=message.concat("Qty : "+Integer.toString(c.getInt(qtyIndex))+"\n");

                itemSummary=itemSummary.concat("Rs."+Integer.toString(c.getInt(qtyIndex) * c.getInt(priceIndex))+"\n");
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
            message=message.concat("\n*Total Items:* "+Integer.toString(i)+"       *Total Price:* Rs."+Integer.toString(totalPrice)+"\n");
            message=message.concat("\n*Customer Details* :\n");
           userData();
        }else{
            item=buyIntent.getStringExtra("item");
            message=message.concat(Integer.toString(i) + ". *"+item+"* | ");
            category=buyIntent.getStringExtra("category");
            message=message.concat(category+" | ");
            qty=buyIntent.getIntExtra("qty",1);
            message=message.concat("Qty : "+qty+"\n");
            itemSummary=itemSummary.concat(Integer.toString(i)+". "+"Item: "+item+"\n     Quantity: "+qty+"    Price: ");
            totalPrice=price*qty;
            itemSummary=itemSummary.concat("Rs."+totalPrice+"\n");
            message=message.concat("\n*Total Items:* 1"+"                 *Total Price:* Rs."+Integer.toString(totalPrice)+"\n");
            message=message.concat("\n*Customer Details* :\n");
            userData();

        }
        itemSummary=itemSummary.concat("\n  Total Items :"+i+"       \n  Total Price :"+"Rs."+totalPrice);
        summaryText.setText(itemSummary);
          
      //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        //    gridView.setAdapter(adapter);

            placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentHour>=11&&currentHour<=20) {
                        if (totalPrice >= 200) {
                            if (addressSave) {
                                // Toast.makeText(PaymentActivity.this, "Address Saved", Toast.LENGTH_SHORT).show();
                                isAppInstalled = isInstalled("com.whatsapp");
                                if (isAppInstalled) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setPackage("com.whatsapp");
                                    i.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + shopNumber + "&text=" + message));
                                    //  startActivity(i);
                                    startActivityForResult(i, 2);
                                } else {
                                    Toast.makeText(PaymentActivity.this, "Please Install WhatsAPP", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(PaymentActivity.this, "Please fill your details", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                                addressSave = (!(sharedPreferences.getBoolean("newUser", true)));
                            }
                        } else {
          //                  Toast.makeText(PaymentActivity.this, "Delivery not available below Rs.200", Toast.LENGTH_SHORT).show();
                            showError("Delivery not available below Rs.200!!");
                        }
                    }else{
                        showError("Delivery available from 11AM to 9PM only!!");
                    }
                }
            });
    }
    private void showError(String errorGet){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Order not send")
                .setMessage(errorGet)
                .setPositiveButton("Ok",null)
                .show();
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
              //  Toast.makeText(this, " name "+cursor.getString(name), Toast.LENGTH_SHORT).show();
//                    name.setText(c.getString(name));
//                    phone.setText(c.getString(phoneIndex));
//                    address.setText(c.getString(addressIndex));
//                    pinCode.setText(Integer.toString(c.getInt(pinCodeIndex)));
                cursor.moveToNext();
//                 sharedPreferences.edit().putBoolean(buyItemName+selectedCategory, false).apply();
//                 15:56
//                 mCartItem.add(cartItem);
                add++;
                message=message.concat("\n*Cash On Delivery* ->Rs. *"+Integer.toString(totalPrice)+"*");
            }
            //message=message.concat("\n*Cash On Delivery* ->Rs. *"+Integer.toString(totalPrice)+"*");
        //add++;
        }catch(Exception e){
            //Toast.makeText(this, "catch", Toast.LENGTH_SHORT).show();
            //newUser=true;
            add=1;
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
        }else{
            startActivity(intent);
        }
    }
    private void makeCall2() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PaymentActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            startActivity(intent2);
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        addressSave = (!(sharedPreferences.getBoolean("newUser", true)));
        if(add==1) {
            //When BACK BUTTON is pressed, the activity on the stack is restarted
            //Do what you want on the refresh procedure here
           // addressSave = (!(sharedPreferences.getBoolean("newUser", true)));
            userData();
           // Toast.makeText(this, "Im am working Hello", Toast.LENGTH_SHORT).show();

           // add++;
        }
       // Toast.makeText(this, "Im am working Hello 2", Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "Im am working Hello", Toast.LENGTH_SHORT).show();
    }
}