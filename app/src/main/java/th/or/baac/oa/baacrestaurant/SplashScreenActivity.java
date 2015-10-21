package th.or.baac.oa.baacrestaurant;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    //Explicit
    private ImageView monkeyImageView;
    private AnimationDrawable objAnimationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        monkeyImageView = (ImageView) findViewById(R.id.imvSplash);
        monkeyImageView.setBackgroundResource(R.anim.monkey);
        objAnimationDrawable = (AnimationDrawable) monkeyImageView.getBackground();
        objAnimationDrawable.start();

        Handler objHandler = new Handler();
        objHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent objIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(objIntent);
                finish();
            }
        }, 5000);

        // Sound Effect
        MediaPlayer introPlayer = MediaPlayer.create(getBaseContext(), R.raw.lion);
        introPlayer.start();
    } // End of main function
} // End of main class
