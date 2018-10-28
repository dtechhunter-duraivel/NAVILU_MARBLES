package com.example.duraivel.fireapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    TextView tv;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);
       tv=(TextView)findViewById(R.id.nm);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ErasBoldITC.ttf");
        tv.setTypeface(typeface);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.downanim);

        tv.startAnimation(anim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the First Page. */
                Intent mainIntent = new Intent(SplashScreen
                        .this,IndexPage.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
