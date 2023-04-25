package com.shekhar.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    Animation topanimation,bottomanimation;
    ImageView image;
    TextView log,slogan;

    private  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topanimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image = findViewById(R.id.image);
        log = findViewById(R.id.log);
        slogan = findViewById(R.id.slogan);

        image.setAnimation(topanimation);
        log.setAnimation(bottomanimation);


        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(log, "logo_text");

                //Check if user signed in or not
                //If signed in then send to HomePage else Login Page
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                if(firebaseAuth.getCurrentUser()==null){
                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(intent, options.toBundle());
                }else{
                    Intent intent = new Intent(MainActivity.this, Homepage.class);
                    startActivity(intent, options.toBundle());
                }

                finish();
            }
        },SPLASH_SCREEN);

    }
}