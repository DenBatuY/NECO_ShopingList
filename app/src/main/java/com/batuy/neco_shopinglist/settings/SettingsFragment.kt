package com.batuy.neco_shopinglist.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.batuy.neco_shopinglist.R

class SettingsFragment:PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference,rootKey)
    }
}