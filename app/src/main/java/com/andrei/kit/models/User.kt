package com.andrei.kit.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
        @PrimaryKey
        @ColumnInfo(name = "userID")
        val userID: String,
        @ColumnInfo(name = "username")
        val username: String,
        @ColumnInfo(name = "email")
        val email: String,
        @ColumnInfo(name = "profilePicture")
        val profilePicture: String

)