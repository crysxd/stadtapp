package stadtapp.hfu.de.stadtapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import de.hfu.stadtapp.R;

public class StartActivity extends Activity implements
        AnimationListener {

    ImageView img1;
    ImageView img2;

    // Animation
    Animation animFadein;
    Animation animFadein2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Timer().schedule(new TimerTask(){
            public void run() {
                StartActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        }, 4000);


        img1 = (ImageView) findViewById(R.id.imgMOS);

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        // set animation listener
        animFadein.setAnimationListener(this);


        img1.setVisibility(View.VISIBLE);
        // start the animation
        img1.startAnimation(animFadein);

    	getActionBar().hide();

    }



    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }
}
