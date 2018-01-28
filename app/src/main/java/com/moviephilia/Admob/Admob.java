package com.moviephilia.Admob;

import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by yogeshtripathi on 24/1/18.
 */

public class Admob {
    private static Activity context;
    private static Admob admob;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    /**
     * @param contextClass pass the activity context
     * @param AppId        pass the Admob App Id
     */
    public static Admob getInstance(Activity contextClass, String AppId) {
        context = contextClass;
        MobileAds.initialize(contextClass, AppId);

        if (admob == null)
            admob = new Admob();
        return admob;


    }

    /**
     * Custom method for loading banner ad...
     *
     * @param mAdView
     */
    public void showAdmobBannerLoad(AdView mAdView) {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Toast.makeText(context, "onAdOpened", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Toast.makeText(context, "onAdLeftApplication", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Toast.makeText(context, "onAdClosed", Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void PauseBannerAd(AdView adViewNew) {


        if (adViewNew != null) adViewNew.pause();


    }

    public void ResumeBannerAd(AdView adViewNew) {
        if (adViewNew != null) adViewNew.resume();


    }

    public void DestroyBannerAd(AdView adViewNew) {
        if (adViewNew != null) adViewNew.destroy();


    }

    /**
     * Custom method to load interstitial  ad...
     *
     * @param interstitialAd
     * @param interstitialId
     */
    public void admobLoadInterstitialAd(InterstitialAd interstitialAd, String interstitialId) {


        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(interstitialId);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        this.mInterstitialAd = interstitialAd;
    }

    /**
     * show interstitial ad..
     *
     * @param interstitialAd
     */
    public void showInterstitialAd(InterstitialAd interstitialAd) {
        if (this.mInterstitialAd != null) {
            if (this.mInterstitialAd.isLoaded()) {
                this.mInterstitialAd.show();
            } else {

                Toast.makeText(context, "The interstitial wasn't loaded yet.", Toast.LENGTH_SHORT).show();
            }


            this.mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.

                    Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    Toast.makeText(context, "onAdOpened", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                    Toast.makeText(context, "onAdLeftApplication", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the user is about to return
                    // to the app after tapping on an ad.

                    // Can be used to Load the next interstitial ad.

                    Toast.makeText(context, "onAdClosed", Toast.LENGTH_SHORT).show();

                }


            });


        }
    }

    public void loadRewardedVideoAd(RewardedVideoAd RewardedVideoAd, String adId) {

        RewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);


        this.mRewardedVideoAd = RewardedVideoAd;

        RewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());


        RewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

            @Override
            public void onRewarded(RewardItem reward) {
                Toast.makeText(context, "onRewarded! currency: " + reward.getType() + "  amount: " +
                        reward.getAmount(), Toast.LENGTH_SHORT).show();
                // Reward the user.
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(context, "onRewardedVideoAdLeftApplication",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(context, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                Toast.makeText(context, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(context, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(context, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(context, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }


        });

    }


    public void showRewardedVideoAd() {
        if (this.mRewardedVideoAd != null && this.mRewardedVideoAd.isLoaded()) {
            this.mRewardedVideoAd.show();
        }

    }

    public void PauseRewardedVideoAd(RewardedVideoAd mRewardedVideoAd) {


        if (mRewardedVideoAd != null) mRewardedVideoAd.pause();


    }

    public void ResumeRewardedVideoAd(RewardedVideoAd mRewardedVideoAd) {
        if (mRewardedVideoAd != null) mRewardedVideoAd.resume();


    }

    public void DestroyRewardedVideoAd(RewardedVideoAd mRewardedVideoAd) {
        if (mRewardedVideoAd != null) mRewardedVideoAd.destroy();


    }


}
