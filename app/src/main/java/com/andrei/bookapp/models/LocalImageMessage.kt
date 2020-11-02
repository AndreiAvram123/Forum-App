package com.andrei.bookapp.models

import android.net.Uri
import com.andrei.dataLayer.dataMappers.UserMapper
import com.andrei.dataLayer.models.UserDTO
import com.andrei.dataLayer.repositories.OperationStatus
import java.util.*

//todo
//man change this
class LocalImageMessage(sender: UserDTO, type: String, localID: String, val resourcePath: Uri)
    : Message(0, "", Calendar.getInstance().timeInMillis/1000L, UserMapper.mapToDomainObject(sender), type, 11, true, localID ) {

    var currentStatus: OperationStatus = OperationStatus.ONGOING
}