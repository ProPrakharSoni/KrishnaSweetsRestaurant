package com.myappcompany.proprakhar.krishnasweets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.*;
import com.google.firebase.auth.GoogleAuthProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =123 ;
    GoogleSignInClient mSignInClient;
    private FirebaseAuth mAuth;
    ImageView chefImage;
    ProgressBar mProgressBar;
    TextView textView;
    FirebaseFirestore fStore;
    String fullName,email,uid;
    SharedPreferences sharedPreferences;
    Boolean isFirstTime;
    ImageView opening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        chefImage =findViewById(R.id.chef);
        mProgressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.krishna_sweets);
        opening=findViewById(R.id.opening);
        sharedPreferences=this.getSharedPreferences("com.myappcompany.proprakhar.krishnasweets",Context.MODE_PRIVATE);
        fStore=FirebaseFirestore.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient =GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sign_in_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
            chefImage.setVisibility(View.INVISIBLE);
            //textView.setVisibility(View.INVISIBLE);
            opening.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_btn).setVisibility(View.INVISIBLE);
            checkUserAccessLevel(mAuth.getCurrentUser().getUid());
        }
    }
    private void signIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("myTag", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        findViewById(R.id.sign_in_btn).setVisibility(View.INVISIBLE);
        chefImage.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
       mProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            fullName=user.getDisplayName();
                            uid=user.getUid();
                            email=user.getEmail();
                            isFirstTime=sharedPreferences.getBoolean("isFirstTime",true);
                            if(isFirstTime) {
                                DocumentReference df = fStore.collection("Users").document(user.getUid());
                                //it does not store duplicate values // while this code runs many time;
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("userEmail", email);
                                df.set(userInfo);
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                            finish();
                            sharedPreferences.edit().putBoolean("isFirstTime",false).apply();
                            }
                            checkUserAccessLevel(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                           // Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void checkUserAccessLevel(String uid){
     DocumentReference df =fStore.collection("Users").document(uid);
     df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {
//             Log.i("val",documentSnapshot.getString("isAdmin"));
             final Calendar c = Calendar.getInstance();
             int mYear = c.get(Calendar.YEAR);
             int mMonth = c.get(Calendar.MONTH)+1;
             int mDay = c.get(Calendar.DAY_OF_MONTH);

//           trial app;;

             if(mYear==2021&&mMonth<=6&&(mDay<=5||mDay==28||mDay==29||mDay==30||mDay==31)) {

              if (documentSnapshot.getString("isAdmin") != null) {
                  startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                  finish();
              } else {
                  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                  finish();
              }
          }
          else{
              System.exit(0);
          }
         }
     });
    }
}