package com.example.dataLayer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.UserMessage
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.RoomDao.MessageDao
import com.example.dataLayer.dataMappers.MessageMapper
import com.example.dataLayer.interfaces.MessagesRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MessageRepository(coroutineScope: CoroutineScope, val application: Application) {

    private val repositoryInterface: MessagesRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(MessagesRepositoryInterface::class.java)
    }
    private val messageDao: MessageDao = PostDatabase.getDatabase(application).messageDao()

    private val messages: HashMap<String, LiveData<List<UserMessage>>> = HashMap()
    val requests: HashMap<String, Boolean> = HashMap()


    fun getMessages(currentUserID: String, user2ID: String): LiveData<List<UserMessage>> {
        val temp = messages[user2ID]
        return if (temp != null) {
            temp;
        } else {
            val toReturn = messageDao.getMessages(currentUserID, user2ID)
            messages[user2ID] = toReturn
            toReturn
        }

    }


    suspend fun fetchRecentMessages(currentUserID: String, receiverID: String) {
        if (AppUtilities.isNetworkAvailable(application)) {
            try {
                val fetchedData = MessageMapper.mapNetworkToDomainObjects(
                        repositoryInterface.fetchOldMessages(currentUserID = currentUserID, receiverID = receiverID, offset = 0)
                )
                requests[receiverID] = true
                messageDao.deleteOldData(currentUserID,receiverID)
                messageDao.insertMessages(fetchedData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun fetchMoreMessages(currentUserID: String, receiverID: String, currentOffset: Int) {
        try {
            val fetchedData = MessageMapper.mapNetworkToDomainObjects(
                    repositoryInterface.fetchOldMessages(currentUserID = currentUserID, receiverID = receiverID, offset = currentOffset))
            messageDao.insertMessages(fetchedData)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}