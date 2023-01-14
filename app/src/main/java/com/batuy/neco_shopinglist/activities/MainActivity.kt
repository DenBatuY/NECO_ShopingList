package com.batuy.neco_shopinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.billing.BillingManager
import com.batuy.neco_shopinglist.databinding.ActivityMainBinding
import com.batuy.neco_shopinglist.dialogs.NewListDialog
import com.batuy.neco_shopinglist.fragments.FragmentManager
import com.batuy.neco_shopinglist.fragments.NoteFragment
import com.batuy.neco_shopinglist.fragments.ShopListNamesFragment
import com.batuy.neco_shopinglist.settings.SettingsActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private var currentTheme = ""
    private var counter = 0
    private var currentMenuItemId = R.id.notes
    private lateinit var defPref: SharedPreferences
    private var iAd: InterstitialAd? = null
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPref.getString("theme_key", "blue").toString()
        setTheme(getSelectedTheme())

        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
        if (!pref.getBoolean(BillingManager.REMOVE_ADS_KEY,false)) loadInterAd()

    }

    private fun loadInterAd() {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.inter_ad_id),
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    iAd = null
                }
            })
    }

    private fun showInterAd(adListener: AdListener) {
        if (iAd != null && counter > 3) {
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }

            }
            counter = 0
            iAd?.show(this)
        } else {
            counter++
            adListener.onFinish()
        }
    }

    private fun setBottomNavListener() {
        binding.btNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }
                    })

                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onClick(name: String) {

    }

    override fun onResume() {
        super.onResume()
        binding.btNav.selectedItemId = currentMenuItemId
        if (defPref.getString("theme_key", "blue") != currentTheme) recreate()
    }

    private fun getSelectedTheme(): Int {
        return if (defPref.getString("theme_key", "blue") == "blue") {
            R.style.Theme_NECO_ShoppingList
        } else {
            R.style.Theme_NECO_ShoppingListGreen
        }
    }

    interface AdListener {
        fun onFinish()
    }
}