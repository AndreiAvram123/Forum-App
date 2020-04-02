package com.example.dataLayer.interfaces

import com.example.dataLayer.models.PostDTO
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface SearchRepositoryInterface {
    @GET("RestfulRequestHandler.php")
    suspend fun fetchSearchSuggestions(@Query("postQuery") query: String): ArrayList<PostDTO>
}