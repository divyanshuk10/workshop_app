package com.divyanshu.workshopapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ContentItems(
    val content: List<Content>
)