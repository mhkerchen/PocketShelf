package com.group.pocketshelf.bookapi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


class GoogleBooksAPI {

    interface GoogleBooksApiService {
        @GET("volumes")
        suspend fun searchBooks(
            @Query("q") searchQuery: String,
            @Query("maxResults") maxResults: Int = 20
        ): Response<DCGoogleBooks1>
    }
}