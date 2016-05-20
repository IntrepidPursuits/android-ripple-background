package com.skyfishjy.ripplebackground.sample;

import android.app.Activity;
import android.os.Bundle;

import com.skyfishjy.library.RippleBackground;
import com.skyfishjy.library.ripplebackground.sample.R;


public class MainActivity extends Activity {

    private RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rippleBackground = (RippleBackground) findViewById(R.id.content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rippleBackground.startRippleAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rippleBackground.stopRippleAnimation();
    }
}
