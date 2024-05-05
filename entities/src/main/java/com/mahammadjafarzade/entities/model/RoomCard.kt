package com.mahammadjafarzade.entities.model

import java.io.Serializable

data class RoomCard(
    val uid : String? = null,
    val image : String? = null,
    val title : String? = null,
    val description : String? = null,
    var isFavorite: Boolean = false,
    val price : Double?= null,
    val city  : String?= null,
    val profileImg : String ?= null,
    val profileTitle : String ?= null,
    val number : String ?=null,
    ): Serializable
