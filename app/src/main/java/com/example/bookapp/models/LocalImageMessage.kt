package com.example.bookapp.models

import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.repositories.UploadProgress

class LocalImageMessage(sender: UserDTO, type: String, localID: String, val resourcePath: Uri)
    : MessageDTO(0, "", 0, sender, type, localID) {

    var currentStatus: UploadProgress = UploadProgress.UPLOADING
}