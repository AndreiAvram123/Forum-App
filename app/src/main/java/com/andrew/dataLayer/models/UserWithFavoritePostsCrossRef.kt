package com.andrew.dataLayer.models

import androidx.room.Entity

@Entity(primaryKeys = ["userID", "postID"])
data class UserWithFavoritePostsCrossRef(
        val postID: Int,
        val userID: Int
)