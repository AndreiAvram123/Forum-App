package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class MessageDTO(
        @SerializedName("messageId")
        val messageID: String,
        @SerializedName("messsageContent")
        val messageContent: String,
        @SerializedName("messageDate")
        val messageDate: Long,
        @SerializedName("senderId")
        val senderID: String,
        @SerializedName("receiverId")
        val receiverID: String,
        @SerializedName("messageImage")
        val messageImage: String
) {
}
