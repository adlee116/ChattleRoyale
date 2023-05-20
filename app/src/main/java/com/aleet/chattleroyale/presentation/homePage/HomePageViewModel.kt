package com.aleet.chattleroyale.presentation.homePage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor() : ViewModel() {

    var playerName: String = ""

    fun process() {

    }


}