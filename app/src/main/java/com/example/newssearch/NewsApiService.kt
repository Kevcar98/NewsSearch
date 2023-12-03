// Import necessary packages and classes
package com.example.newssearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Define the NewsApiService interface
interface NewsApiService {

    // Define a function annotated with @GET for making a GET request to the News API
    // The relative URL is "v2/everything"
    // The function parameters are annotated with @Query to specify query parameters
    @GET("v2/everything")
    fun getNews(
        @Query("q") query: String,        // Query parameter for the search query
        @Query("from") date: String,      // Query parameter for the start date
        @Query("to") date1: String,       // Query parameter for the end date
        @Query("language") language: String,  // Query parameter for the language
        @Query("apiKey") apiKey: String   // Query parameter for the API key
    ): Call<NewsResponse>
}
