package com.mahammadjafarzade.entities.model

import java.io.Serializable

data class UserData(
    val uid : String? = null,
    val fullName : String ?= null,
    val email : String ?= null,
    val number : String ?= null,
    val password : String ?= null
): Serializable