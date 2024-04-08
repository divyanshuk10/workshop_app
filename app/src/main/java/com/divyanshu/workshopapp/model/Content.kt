package com.divyanshu.workshopapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val name: String,
    @SerialName("poster-image")
    val posterImage: String
)