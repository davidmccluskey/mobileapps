package com.example.dogwalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Around halfway through development, after adding my Dog Info tab (with glide images) and my google map
 * setup, I realised that the app was taking ~8 seconds to launch, leaving the app (and user) to hang with a white screen.
 * So I created a splashscreen, which waits until the MainActivity(and subsequently all of my fragments)
 * have loaded and launches the app once they've finished.
 */

public class SplashScreen extends AppCompatActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); //The splashscreen is a static image with a biege background.

        Thread welcomeThread = new Thread()
        {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);  //Delay of 5 seconds to hide loading.
                    // Loading could be done before this, but I don't want to drop the splashscreen
                    // with a white screen displayed if loading for whatever reason takes longer.

                } catch (Exception e)
                {
                } finally //finally used to launch app once sleep is done.
                {

                    Intent i = new Intent(SplashScreen.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
