package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class SerializeImage(@SerializedName("image")
                       val imageData: String,
                          @SerializedName("extension")
                       val extension: String?)
