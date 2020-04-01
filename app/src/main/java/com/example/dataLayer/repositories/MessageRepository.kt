package com.example.dataLayer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.UserMessage
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.RoomDao.MessageDao
import com.example.dataLayer.dataMappers.MessageMapper
import com.example.dataLayer.interfaces.MessagesRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MessageRepository(coroutineScope: CoroutineScope, application: Application) {

    private val repositoryInterface: MessagesRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(MessagesRepositoryInterface::class.java)
    }
    private val messageDao: MessageDao = PostDatabase.getDatabase(application).messageDao()

    private val messages: HashMap<String, LiveData<PagedList<UserMessage>>> = HashMap()
    val requests: HashMap<String, Boolean> = HashMap()

    val configuration:PagedList.Config = PagedList.Config.Builder()
           .setPageSize(20)
           .setInitialLoadSizeHint(10)
           .setPrefetchDistance(10)
           .setEnablePlaceholders(true)
           .build()

    fun getMessages(currentUserID: String, user2ID: String): LiveData<PagedList<UserMessage>> {
        val factory = messageDao.getAllMessages(currentUserID, user2ID)
        val temp = LivePagedListBuilder(factory, configuration).build()
        messages[user2ID] = temp
        return temp
    }



suspend fun fetchMessages(currentUserID: String, receiverID: String) {
        try {
            val fetchedData = MessageMapper.mapNetworkToDomainObjects(
                    repositoryInterface.fetchOldMessages(currentUserID = currentUserID, receiverID = receiverID, offset = 0)
            )
            requests[receiverID] = true
            messageDao.insertMessages(fetchedData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}