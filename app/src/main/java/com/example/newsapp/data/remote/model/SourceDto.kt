package com.example.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class SourceDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)
