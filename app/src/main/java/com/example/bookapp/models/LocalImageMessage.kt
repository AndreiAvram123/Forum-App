package com.example.bookapp.models

import android.net.Uri
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.repositories.UploadProgress
import java.util.*

//todo
//man change this
class LocalImageMessage(sender: UserDTO, type: String, localID: String, val resourcePath: Uri)
    : Message(0, "", Calendar.getInstance().timeInMillis/1000L, UserMapper.mapToDomainObject(sender), type, 11, true, localID ) {

    var currentStatus: UploadProgress = UploadProgress.UPLOADING
}