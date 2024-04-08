package com.divyanshu.workshopapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page(
    @SerialName("content-items")
    val contentItems: ContentItems,
    @SerialName("page-num")
    val pageNum: String,
    @SerialName("page-size")
    val pageSize: String,
    val title: String,
    @SerialName("total-content-items")
    val totalContentItems: String
)