package com.example.bookapp.models

import android.net.Uri
import com.example.dataLayer.dataMappers.toUser
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.repositories.OperationStatus
import java.util.*

class LocalImageMessage(sender: UserDTO, type: String, localID: String, val resourcePath: Uri)
    : Message(0, "", Calendar.getInstance().timeInMillis / 1000L, sender.toUser(), type, 11, true, localID) {

    var currentStatus: OperationStatus = OperationStatus.ONGOING
}