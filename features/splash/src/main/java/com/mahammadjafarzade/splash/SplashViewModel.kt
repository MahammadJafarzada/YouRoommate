package com.mahammadjafarzade.splash

import androidx.lifecycle.ViewModel
import com.mahammadjafarzade.common.util.MySharedPreferencesInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val preferences: MySharedPreferencesInterface) : ViewModel()  {
    fun saveIntroPassed(){
        preferences.saveString("intro","1")
    }
    val isIntroPlayed : Boolean get(){
        return preferences.getString("intro","0") == "1"
    }
}