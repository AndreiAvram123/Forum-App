package com.example.bookapp.models

import android.graphics.drawable.Drawable
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.repositories.UploadProgress

class LocalImageMessage(id: Int ,sender: UserDTO, type: String, localID: String, val drawable: Drawable)
    : MessageDTO(id, "", 0, sender, type, localID) {

    var currentStatus: UploadProgress = UploadProgress.UPLOADING
}