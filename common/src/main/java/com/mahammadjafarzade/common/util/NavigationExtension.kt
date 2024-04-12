package com.mahammadjafarzade.common.util

import android.net.Uri
import androidx.navigation.NavController

fun NavController.toLogin(){
    val uri = Uri.parse("youroommate://toLogin/")
    this.navigate(uri)
}
fun NavController.toRegister(){
    val uri = Uri.parse("youroommate://toRegister/")
    this.navigate(uri)
}

fun NavController.toMain(){
    val uri = Uri.parse("youroommate://toMain/")
    this.navigate(uri)
}