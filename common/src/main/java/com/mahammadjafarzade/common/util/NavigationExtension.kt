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

fun NavController.toHome(){
    val uri = Uri.parse("youroommate://toHome/")
    this.navigate(uri)
}
fun NavController.toFavourite(){
    val uri = Uri.parse("youroommate://toFavourite/")
    this.navigate(uri)
}
fun NavController.toChat(){
    val uri = Uri.parse("youroommate://toChat/")
    this.navigate(uri)
}
fun NavController.toAccount(){
    val uri = Uri.parse("youroommate://toAccount /")
    this.navigate(uri)
}

