package com.wallet.crypto.trustapp.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.wallet.crypto.trustapp.R;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        int fontColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        addSlide(AppIntroFragment.newInstance("Private & Secure", null, "Private keys never leave your device.", null, R.mipmap.onboarding_lock, Color.WHITE, fontColor, fontColor));
        addSlide(AppIntroFragment.newInstance("Fully Transparent", null, "Code is open sourced (GPL-3.0 license) and fully audited.", null, R.mipmap.onboarding_open_source, Color.WHITE, fontColor, fontColor));
        addSlide(AppIntroFragment.newInstance("Ultra reliable", null, "The fastest Ethereum wallet experience on mobile.", null, R.mipmap.onboarding_rocket, Color.WHITE, fontColor, fontColor));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done generateButton.
        //showSkipButton(true);
        //setProgressButtonEnabled(false);
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done generateButton.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
