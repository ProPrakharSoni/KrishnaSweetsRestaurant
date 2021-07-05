package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class AdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;
    Button logout,gallery,mUploadButton,userView;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    Spinner mItemSpinner;
    ProgressBar mProgressBarImageUploading,checkUploadProgress;
    private Uri mImageUri;
    Bitmap bitmap;
    private ImageView mImageView;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String itemName,category1,category2;
    private int priceCategory1,priceCategory2;
    private UploadTask uploadTask2;
    EditText itemNameEditText,category1EditText,category2EditText,priceCategory1EditText,priceCategory2EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        itemNameEditText=findViewById(R.id.itemNameEditText);
        userView=findViewById(R.id.userView);
        checkUploadProgress=findViewById(R.id.checkUploadProgress);
        category1EditText=findViewById(R.id.category1EditText);
        category2EditText=findViewById(R.id.category2EditText);
        priceCategory1EditText=findViewById(R.id.price1EditText);
        priceCategory2EditText=findViewById(R.id.price2EditText);
        mItemSpinner=findViewById(R.id.spinner);
        mProgressBarImageUploading=findViewById(R.id.progressBarForImageUploading);
        mUploadButton=findViewById(R.id.uploadButton);
        gallery= findViewById(R.id.gallery);
        mImageView=findViewById(R.id.imageToUpload);
        checkUploadProgress.setVisibility(View.INVISIBLE);
        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        String[] itemCategories={"Pasta","FriedRice","Chinese","Noodles","Burger","PavBaji","IceCream","Dhhosa","Soup","ColdDrink","Bakery","Nasta","Sweets","Pizza","Other Items","Offers"};
        Arrays.sort(itemCategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,itemCategories);
        mItemSpinner.setAdapter(adapter);
        logout = findViewById(R.id.logoutAdmin);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBarImageUploading.setProgress(0);
                itemNameEditText.setText("");
                category1EditText.setText("");
                category2EditText.setText("");
                priceCategory1EditText.setText("");
                priceCategory2EditText.setText("");
                  openFileChoosen();
            }
        });
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter=0;
                if(uploadTask2!=null&&uploadTask2.isInProgress())
                {
                    Toast.makeText(getApplicationContext(), "Upload In Progress", Toast.LENGTH_SHORT).show();
                }else {
                    if (itemNameEditText.getText().toString().isEmpty()) {
                        itemNameEditText.setError("Error");
                        counter++;
                    }
                    if (priceCategory1EditText.getText().toString().isEmpty()) {
                        priceCategory1EditText.setError("Error");
                        counter++;
                    }
                    if(category1EditText.getText().toString().isEmpty()){
                        category1EditText.setError("Error");
                        counter++;
                    }
                    if (counter == 0) {
                        mStorageRef = FirebaseStorage.getInstance().getReference(mItemSpinner.getSelectedItem().toString());
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mItemSpinner.getSelectedItem().toString());
                        checkUploadProgress.setVisibility(View.VISIBLE);
                        uploadFile();
                    }
                }
            }
        });

    }

    private void openFileChoosen() {
        Intent intent = new Intent();
        intent.setType("image/*");// by this it shows only images to file chooser
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            //     mImageView.setImageURI(mImageUri);
            //
            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                mImageView.setImageBitmap(bitmap);
            }catch(Exception e){
                e.printStackTrace();
            }
            //
        }
    }
    private void signOut() {
        // Firebase sign out
        try {
            mAuth.signOut();
            // Google sign out
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finishAffinity();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            finish();
                        }
                    });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // uploading file
    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(itemNameEditText.getText().toString() + "." + getFileExtension(mImageUri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            uploadTask2 = fileReference.putBytes(data);
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //  Toast.makeText(Profilepic.this, "Upload successful", Toast.LENGTH_LONG).show();
                    //Toast.makeText(MainActivity.this, "Uploaded Succesfully", Toast.LENGTH_SHORT).show();


//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgressBarImageUploading.setProgress(0);
//                        }
//                    },500);

                    //checkUploadProgress.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();

                    //
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();
                            // picUrl=downloadUrl;
                            //  Log.i("AMan",downloadUrl);
                            //  Toast.makeText(MainActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();
                            Upload upload=new Upload(itemNameEditText.getText().toString().trim(),category1EditText.getText().toString().trim(),category2EditText.getText().toString().trim(),downloadUrl,priceCategory1EditText.getText().toString(),priceCategory2EditText.getText().toString());

                            String uploadId = mDatabaseRef.push().getKey();
                           mDatabaseRef.child(itemNameEditText.getText().toString()).setValue(upload);
                            //mDatabaseRef.child(uploadId).setValue(upload);

                            checkUploadProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                        }});
                    //
                    //Log.i("AMan",picUrl);
                    //yha se
                    //
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //  Toast.makeText(Profilepic.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                    checkUploadProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Upload fail", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    mProgressBarImageUploading.setProgress((int) progress);
                }
            });
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    }