package com.optic.ecommerceappmvvm.ads


import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager(private val context: Context) {

    private var rewardedAd: RewardedAd? = null

    fun loadAd() {
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",  // ID de prueba
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    fun showAd(activity: Activity, onReward: (Int) -> Unit) {
        rewardedAd?.show(activity) { rewardItem ->
            onReward(rewardItem.amount)
        }
    }
}
